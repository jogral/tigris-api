import graphene
import graphql_jwt
from graphene_django.debug import DjangoDebug
from learn import schema as LearnSchema
from users import schema as UserSchema


class Query(LearnSchema.Query,
            UserSchema.Query,
            graphene.ObjectType):
    debug = graphene.Field(DjangoDebug, name='__debug')


class Mutation(LearnSchema.Mutation,
               UserSchema.Mutation,
               graphene.ObjectType):
    token_auth = graphql_jwt.ObtainJSONWebToken.Field()
    verify_token = graphql_jwt.Verify.Field()
    refresh_token = graphql_jwt.Refresh.Field()

schema = graphene.Schema(query=Query, mutation=Mutation)
