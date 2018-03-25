# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.utils.text import slugify
from django.utils.translation import ugettext_lazy as _
from django.contrib.postgres.fields import (
    CICharField,
)

from users.models import User

CONTENT_CHOICES = (
    ('D', 'Deleted'),
    ('U', 'Unpublished'),
    ('P', 'Published'),
)
DELETED = 'D'
UNPUBLISHED = 'U'
PUBLISHED = 'P'


class Tag(models.Model):
    name = CICharField(
        verbose_name='Tag name',
        max_length=32,
        help_text=_('Tag name'),
        unique=True
    )
    created = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return self.name


class Course(models.Model):
    title = models.CharField(
        verbose_name='Course Title',
        max_length=512,
        help_text=_('The course title'),
        unique=True
    )
    slug = models.SlugField(max_length=64, unique=True)
    teaser = models.CharField(
        verbose_name='Course content teaser',
        max_length=512,
        blank=True,
        help_text=_('A brief, one-sentence description of the course.')
    )
    description = models.CharField(
        verbose_name='Course description',
        max_length=1024,
        help_text=_('A description of the course.')
    )
    long_description = models.TextField(
        verbose_name='Course long description',
        blank=True,
        help_text=_('A longer description of the course.')
    )
    tags = models.ManyToManyField(Tag)
    image = models.ImageField(
        upload_to="img/courses/",
        max_length=512,
        height_field=None,
        width_field=None,
        blank=True
    )
    status = models.CharField(
        max_length=1,
        choices=CONTENT_CHOICES,
        default=UNPUBLISHED,
    )
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)
    creator = models.ForeignKey(User, on_delete=models.CASCADE)

    def __str__(self):
        return self.title

    @property
    def is_active(self):
        return self.status == PUBLISHED

    def save(self, *args, **kwargs):
        if not self.pk:
            self.slug = slugify(self.title)
        super(Course, self).save(*args, **kwargs)


class Module(models.Model):
    title = models.CharField(
        verbose_name='Course Title',
        max_length=512,
        help_text=_('The course title'),
        unique=True
    )
    slug = models.SlugField(max_length=64, unique=True)
    content = models.TextField()
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    order_index = models.PositiveSmallIntegerField()
    status = models.CharField(
        max_length=1,
        choices=CONTENT_CHOICES,
        default=UNPUBLISHED,
    )
    created = models.DateTimeField(auto_now_add=True)
    updated = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.title

    @property
    def is_active(self):
        return self.status == PUBLISHED

    def save(self, *args, **kwargs):
        if not self.pk:
            self.slug = slugify(self.title)
        if not self.order_index or \
           Module.objects.filter(order_index=self.order_index).exists():
            self.order_index = len(
                list(Module.objects.filter(course__id=self.course))
            ) + 1
        super(Module, self).save(*args, **kwargs)
