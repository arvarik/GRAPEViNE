# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0016_auto_20151213_1315'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='exactLocation',
            field=models.CharField(max_length=80),
        ),
        migrations.AlterField(
            model_name='feed',
            name='exactLocation',
            field=models.CharField(max_length=80),
        ),
    ]
