# Generated by Django 2.0.3 on 2018-03-25 00:58

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('learn', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='course',
            name='long_description',
            field=models.TextField(blank=True, help_text='A longer description of the course.', verbose_name='Course long description'),
        ),
    ]
