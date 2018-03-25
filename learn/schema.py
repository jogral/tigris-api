from graphene import (
    relay,
    Mutation,
    ObjectType,
    ID,
    Int,
    List,
    String,
)
from graphene_django.types import (
    DjangoObjectType,
)
from graphene_django.filter import DjangoFilterConnectionField

from .models import (
    Tag,
    Course,
    Module,
)


class TagNode(DjangoObjectType):
    class Meta:
        model = Tag
        filter_fields = ['name', ]
        interfaces = (relay.Node, )


class UpsertTag(Mutation):
    tag = relay.Node.Field(TagNode)

    class Arguments:
        name = String(required=True)

    def mutate(self, info, name):
        tag, _ = Tag.objects.get_or_create(name=name)
        tag.save()

        return UpsertTag(tag=tag)


class CourseNode(DjangoObjectType):
    class Meta:
        model = Course
        filter_fields = {
            'title': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'slug': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
            'creator': ['exact'],
            'creator__email': ['exact', 'iexact', 'icontains', 'istartswith', ],
        }
        interfaces = (relay.Node, )


class UpsertCourse(Mutation):
    course = relay.Node.Field(CourseNode)

    class Arguments:
        title = String(required=True)
        slug = String()
        teaser = String()
        description = String(required=True)
        long_description = String(required=True)
        tags = List(ID)
        status = String()
        creator = ID(required=True)

    def mutate(self,
               info,
               title,
               slug,
               teaser,
               description,
               long_description,
               tags,
               status,
               creator):
        course, _ = Course.objects.get_or_create(
            title=title,
            teaser=teaser,
            description=description,
            long_description=long_description,
            creator=creator,
        )
        if slug:
            course.slug = slug
        if tags:
            course.tags = tags
        if status:
            course.status = status
        course.save()

        return UpsertCourse(course=course)


class ModuleNode(DjangoObjectType):
    class Meta:
        model = Module
        filter_fields = {
            'title': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'slug': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'course': ['exact'],
            'course__title': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
        }
        interfaces = (relay.Node, )


class UpsertModule(Mutation):
    course = relay.Node.Field(ModuleNode)

    class Arguments:
        title = String(required=True)
        slug = String()
        content = String(required=True)
        order_index = Int()
        course = ID(required=True)
        status = String()
        creator = ID(required=True)

    def mutate(self,
               info,
               title,
               slug,
               content,
               order_index,
               course,
               status,
               creator):
        module, _ = Module.objects.get_or_create(
            title=title,
            content=content,
            order_index=order_index,
            creator=creator,
        )
        if order_index:
            module.order_index = order_index
        if slug:
            module.slug = slug
        if status:
            module.status = status
        module.save()

        return UpsertModule(module=module)


class Mutation(ObjectType):
    upsert_tag = UpsertTag.Field()
    upsert_course = UpsertCourse.Field()
    upsert_module = UpsertModule.Field()


class Query(ObjectType):
    tags = DjangoFilterConnectionField(TagNode)
    tag = relay.Node.Field(TagNode)

    courses = DjangoFilterConnectionField(CourseNode)
    course = relay.Node.Field(CourseNode)

    modules = DjangoFilterConnectionField(ModuleNode)
    module = relay.Node.Field(ModuleNode)

    def resolve_courses(self, info, **kwargs):
        return Course.objects.filter(status__in=['U', 'P'])

    def resolve_modules(self, info, **kwargs):
        return Module.objects.filter(status__in=['U', 'P'])
