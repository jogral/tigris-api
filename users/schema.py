from django.contrib.auth import get_user_model
from graphene import (
    relay,
    Field,
    ID,
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
            'username': ['exact', 'iexact'],
            'email': ['exact', 'iexact'],
            'is_active': ['exact'],
        }
        interfaces = (relay.Node, )


class CreateUser(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        email = String(required=True)
        password = String(required=True)
        username = String()

    def mutate(self, info, email, password, username):
        shortname = email
        if username is not None:
            shortname = username
        user = get_user_model()(
            email=email,
            username=shortname,
        )
        user.set_password(password)
        user.save()

        return CreateUser(user=user)


class UpdatePassword(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        id = ID(required=True)
        password = String(required=True)

    def mutate(self, info, id, password):
        pk = id
        user = get_user_model()(
            pk=pk
        )
        user.set_password(password)
        user.save()

        return UpdatePassword(user=user)


class UpdateUser(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        id = ID(required=True)
        username = String()
        email = String()
        first_name = String()
        last_name = String()
        phone_number = String()

    def mutate(self,
               info,
               id,
               username,
               email,
               first_name,
               last_name,
               phone_number):
        pk = id
        user = get_user_model()(
            pk=pk
        )
        if username:
            user.username = username
        if email and email != '':
            user.email = email
        if first_name:
            user.first_name = first_name
        if last_name:
            user.last_name = last_name
        if phone_number:
            user.phone_number = phone_number
        user.save()

        return UpdateUser(user=user)


class DeleteUser(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        id = ID(required=True)

    def mutate(self, info, id):
        pk = id
        user = get_user_model()(
            pk=pk
        )
        user.is_active = False
        user.save()

        return DeleteUser(user=user)


class UndeleteUser(Mutation):
    user = relay.Node.Field(UserNode)

    class Arguments:
        id = ID(required=True)

    def mutate(self, info, id):
        pk = id
        user = get_user_model()(
            pk=pk
        )
        user.is_active = True
        user.save()

        return UndeleteUser(user=user)


class Mutation(ObjectType):
    create_user = CreateUser.Field()
    delete_user = DeleteUser.Field()
    undelete_user = UndeleteUser.Field()
    update_password = UpdatePassword.Field()
    update_user = UpdateUser.Field()


class Query(ObjectType):
    me = Field(UserNode)
    users = DjangoFilterConnectionField(UserNode)
    user = relay.Node.Field(UserNode)

    def resolve_users(self, info, **kwargs):
        return get_user_model().objects.filter(is_active=True)

    def resolve_me(self, info):
        user = info.context.user
        if user.is_anonymous:
            raise Exception('Not logged in.')

        return user
