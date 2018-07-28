from django.contrib.auth import get_user_model
from graphene import (
    relay,
    Field,
    Mutation,
    ObjectType,
    String,
)
from graphene_django.converter import convert_django_field
from graphene_django.types import (
    DjangoObjectType,
)
from graphene_django.filter import DjangoFilterConnectionField

from phonenumber_field.modelfields import PhoneNumberField


@convert_django_field.register(PhoneNumberField)
def convert_phone_number_field_to_string(field, registry=None):
    return String()


class UserNode(DjangoObjectType):
    class Meta:
        model = get_user_model()
        filter_fields = {
            'email': ['exact', 'iexact'],
            'is_active': ['exact'],
        }
        interfaces = (relay.Node, )


class CreateUser(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        email = String(required=True)
        password = String(required=True)

    def mutate(self, info, email, password):
        user = get_user_model()(
            email=email,
            username=email,
        )
        user.set_password(password)
        user.save()

        return CreateUser(user=user)


class Mutation(ObjectType):
    create_user = CreateUser.Field()


class Query(ObjectType):
    me = Field(UserNode)
    users = DjangoFilterConnectionField(UserNode)

    def resolve_users(self, info, **kwargs):
        return get_user_model().objects.filter(is_active=True)

    def resolve_me(self, info):
        user = info.context.user
        if user.is_anonymous:
            raise Exception('Not logged in.')

        return user
