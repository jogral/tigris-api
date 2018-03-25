from django.contrib.auth.base_user import BaseUserManager
from rolepermissions.roles import assign_role


class UserManager(BaseUserManager):
    def create_user(self, email, group_name='students', password=None):
        """
        Creates and saves a User with the given email and password.
        """
        if not email:
            raise ValueError('Users must have an email address')

        user = self.model(
            email=self.normalize_email(email),
        )

        user.set_password(password)
        user.save(using=self._db)
        if group_name.lower() == 'admins':
            assign_role(user, 'admin')
        elif group_name.lower() == 'instructors':
            assign_role(user, 'instructor')
        else:
            assign_role(user, 'student')
        user.save(using=self._db)
        return user

    def create_superuser(self, email, password):
        """
        Creates and saves a superuser with the given email and password.
        """
        user = self.create_user(
            email,
            password=password,
            group_name='admins'
        )
        user.is_superuser = True
        user.save()
        return user
