# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0014_auto_20151210_2322'),
    ]

    operations = [
        migrations.RenameField(
            model_name='feed',
            old_name='title',
            new_name='name',
        ),
    ]
