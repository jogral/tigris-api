from rolepermissions.roles import AbstractUserRole


class Student(AbstractUserRole):
    available_permissions = {
    }


class Instructor(AbstractUserRole):
    available_permissions = {
        'learn_add_course': True,
        'learn_change_course': True,
        'learn_delete_course': True,
        'learn_add_module': True,
        'learn_change_module': True,
        'learn_delete_module': True,
        'assess_add_test': True,
        'assess_change_test': True,
        'assess_delete_test': True,
        'assess_add_quiz': True,
        'assess_change_quiz': True,
        'assess_delete_quiz': True,
        'session_change_enrollment': True,
    }


class Admin(AbstractUserRole):
    available_permissions = {
        'learn_add_course': True,
        'learn_change_course': True,
        'learn_delete_course': True,
        'learn_add_module': True,
        'learn_change_module': True,
        'learn_delete_module': True,
        'assess_add_test': True,
        'assess_change_test': True,
        'assess_delete_test': True,
        'assess_add_quiz': True,
        'assess_change_quiz': True,
        'assess_delete_quiz': True,
        'session_change_enrollment': True,
        'session_delete_enrollment': True,
        'users_add_user': True,
        'users_change_user': True,
        'users_delete_user': True,
        'change_role': True,
    }
