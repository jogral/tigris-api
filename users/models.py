from django.db import models
from django.contrib.auth.models import PermissionsMixin
from django.contrib.auth.base_user import AbstractBaseUser
from django.contrib.postgres.fields import (
    CICharField,
)
from django.utils.translation import ugettext_lazy as _

from phonenumber_field.modelfields import PhoneNumberField
from rolepermissions.checkers import (
    has_permission,
    has_role
)

from .managers import UserManager


class User(AbstractBaseUser, PermissionsMixin):
    username = CICharField(
        verbose_name='optional username',
        max_length=127,
        unique=True,
    )
    email = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,
    )
    first_name = models.CharField(
        verbose_name='first name',
        help_text=_('The first (given) name.'),
        blank=True,
        max_length=128
    )
    last_name = models.CharField(
        verbose_name='last name',
        help_text=_('The surname or family name.'),
        blank=True,
        max_length=128
    )
    phone_number = PhoneNumberField(blank=True)
    is_active = models.BooleanField(default=True)
    is_superuser = models.BooleanField(default=False)

    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    def __str__(self):
        return self.email

    def has_perm(self, perm, obj=None):
        "Does the user have a specific permission?"
        # Simplest possible answer: Yes, always
        return has_permission(self, perm.replace('\.', '_'))

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        # Simplest possible answer: Yes, always
        return True

    @property
    def is_student(self):
        return has_role(self, 'student')

    @property
    def is_instructor(self):
        return has_role(self, 'instructor')

    @property
    def is_admin(self):
        return has_role(self, 'admin')

    @property
    def is_staff(self):
        return self.is_admin or self.is_superuser

    def save(self, *args, **kwargs):
        if not self.username:
            self.username = self.email
        super(User, self).save(*args, **kwargs)
