from graphene import (
    relay,
    Mutation,
    ObjectType,
    Boolean,
    ID,
    String,
)
from graphene_django.types import (
    DjangoObjectType,
)
from graphene_django.filter import DjangoFilterConnectionField

from .models import (
    Question,
    Answer,
    Quiz,
    Exam,
)


# SEC: Question

class QuestionNode(DjangoObjectType):
    class Meta:
        model = Question
        filter_fields = {
            'assessment': ['exact'],
            'question_type': ['exact', 'iexact', ],
            'value': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
            'creator': ['exact'],
            'creator__email': ['exact', 'iexact', 'icontains', 'istartswith', ],
        }
        interfaces = (relay.Node, )


class UpsertQuestion(Mutation):
    question = relay.Node.Field(QuestionNode)

    class Arguments:
        assessment = ID(required=True)
        question_type = String()
        value = String(required=True)
        status = String()
        creator = ID(required=True)

    def mutate(self,
               assessment,
               value,
               creator,
               question_type,
               status):
        question, _ = Question.objects.get_or_create(
            assessment=assessment,
            value=value,
            creator=creator,
        )
        if question_type:
            question.question_type = question_type
        if status:
            question.status = status
        question.save()

        return UpsertQuestion(question=question)


# SEC: Answer

class AnswerNode(DjangoObjectType):
    class Meta:
        model = Answer
        filter_fields = {
            'question_type': ['exact', 'iexact', ],
            'value': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
            'creator': ['exact'],
            'creator__email': ['exact', 'iexact', 'icontains', 'istartswith', ],
        }
        interfaces = (relay.Node, )


class UpsertAnswer(Mutation):
    question = relay.Node.Field(AnswerNode)

    class Arguments:
        question = ID(required=True)
        value = String(required=True)
        status = String()
        # creator = ID(required=True)
        correct = Boolean()

    def mutate(self,
               question,
               value,
               status,
               # creator,
               correct):
        answer, _ = Answer.objects.get_or_create(
            question=question,
            value=value,
            # creator=creator,
        )
        if correct:
            answer.correct = correct
        if status:
            answer.status = status
        answer.save()

        return UpsertAnswer(answer=answer)


# SEC: Quiz

class QuizNode(DjangoObjectType):
    class Meta:
        model = Quiz
        filter_fields = {
            'module': ['exact'],
            'module__slug': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
            'creator': ['exact'],
            'creator__email': ['exact', 'iexact', 'icontains', 'istartswith', ],
        }
        interfaces = (relay.Node, )


class UpsertQuiz(Mutation):
    quiz = relay.Node.Field(QuizNode)

    class Arguments:
        name = String()
        module = ID(required=True)
        status = String()
        creator = ID(required=True)

    def mutate(
            self,
            module,
            creator,
            name,
            status
    ):
        quiz, _ = Quiz.objects.get_or_create(
            module=module,
            creator=creator,
        )
        if name:
            quiz.name = name
        if status:
            quiz.status = status
        quiz.save()

        return UpsertQuiz(quiz=quiz)


# SEC: Exam

class ExamNode(DjangoObjectType):
    class Meta:
        model = Exam
        filter_fields = {
            'course': ['exact'],
            'course__slug': ['exact', 'iexact', 'icontains', 'istartswith', ],
            'created': ['exact', 'gte', 'lte', ],
            'updated': ['exact', 'gte', 'lte', ],
            'status': ['exact'],
            'creator': ['exact'],
            'creator__email': ['exact', 'iexact', 'icontains', 'istartswith', ],
        }
        interfaces = (relay.Node, )


class UpsertExam(Mutation):
    exam = relay.Node.Field(ExamNode)

    class Arguments:
        name = String()
        course = ID(required=True)
        status = String()
        creator = ID(required=True)

    def mutate(
            self,
            name,
            course,
            status,
            creator
    ):
        exam, _ = Exam.objects.get_or_create(
            course=course,
            creator=creator
        )
        if name:
            exam.name = name
        if status:
            exam.status = status
        exam.save()

        return UpsertExam(exam=exam)


# SEC: Mutation

class Mutation(ObjectType):
    upsert_question = UpsertQuestion.Field()
    upsert_answer = UpsertAnswer.Field()
    upsert_quiz = UpsertQuiz.Field()
    upsert_answer = UpsertAnswer.Field()


# SEC: Query

class Query(ObjectType):
    questions = DjangoFilterConnectionField(QuestionNode)
    question = relay.Node.Field(QuestionNode)

    answers = DjangoFilterConnectionField(AnswerNode)
    answer = relay.Node.Field(AnswerNode)

    def resolve_questions(self, info, **kwargs):
        return Question.objects.filter(status__in=['U', 'P'])

    def resolve_answers(self, info, **kwargs):
        return Answer.objects.filter(status__in=['U', 'P'])
