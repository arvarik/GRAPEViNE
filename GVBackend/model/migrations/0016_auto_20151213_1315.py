# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0015_auto_20151212_1731'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='exactLocation',
            field=models.CommaSeparatedIntegerField(max_length=80),
        ),
        migrations.AlterField(
            model_name='feed',
            name='exactLocation',
            field=models.CommaSeparatedIntegerField(max_length=80),
        ),
    ]
