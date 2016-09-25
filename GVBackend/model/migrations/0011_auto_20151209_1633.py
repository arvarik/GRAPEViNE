# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0010_auto_20151204_1217'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='dateCreated',
            field=models.DateTimeField(),
        ),
    ]
