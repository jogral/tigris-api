from django.db import models
from django.utils.translation import ugettext_lazy as _
from django.contrib.postgres.fields import (
    CICharField,
)
from tigris.utils import generate_rand_str
from learn.models import (
    Module,
    Course,
    CONTENT_CHOICES,
    UNPUBLISHED,
)
from users.models import User


QUESTION_CHOICES = (
    ('MA', 'Multiple Answer'),
    ('MC', 'Multiple Choice'),
    ('CODE', 'Code'),
    ('FITB', 'Fill-in-the-blank'),
    ('SA', 'Short Answer'),
    ('ESS', 'Essay'),
)

QUIZ_OR_EXAM = (
    ('Q', 'Quiz'),
    ('E', 'Exam'),
)

MULT_ANSWER = 'MA'
MULT_CHOICE = 'MC'
CODE_ANSWER = 'CODE'
FILL_IN_THE_BLANK = 'FITB'
SHORT_ANSWER = 'SA'
ESSAY = 'ESS'

QUIZ = 'Q'
EXAM = 'E'


class Assessment(models.Model):
    name = CICharField(
        verbose_name='Assessment name',
        max_length=512,
        help_text=_('What you will call the quiz or test.'),
    )
    status = models.CharField(
        max_length=1,
        choices=CONTENT_CHOICES,
        default=UNPUBLISHED,
    )
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    creator = models.ForeignKey(User, on_delete=models.CASCADE)

    def save(self, *args, **kwargs):
        if not self.name:
            self.name = ' '.join(['Quiz', generate_rand_str(16)])
        super(Assessment, self).save(*args, **kwargs)


class Question(models.Model):
    assessment = models.ForeignKey(Assessment, on_delete=models.CASCADE)
    quiz_or_exam = models.CharField(
        max_length=1,
        choices=QUIZ_OR_EXAM,
        default=QUIZ
    )
    question_type = models.CharField(
        max_length=4,
        choices=QUESTION_CHOICES,
        default=FILL_IN_THE_BLANK,
    )
    value = models.TextField()
    status = models.BooleanField()
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    creator = models.ForeignKey(User, on_delete=models.CASCADE)


class Answer(models.Model):
    question = models.ForeignKey(Question, on_delete=models.CASCADE)
    value = models.TextField()
    correct = models.BooleanField(default=False)
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    # creator = models.ForeignKey(User, on_delete=models.CASCADE)


class Quiz(Assessment):
    module = models.ForeignKey(Module, on_delete=models.CASCADE)


class Exam(Assessment):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
