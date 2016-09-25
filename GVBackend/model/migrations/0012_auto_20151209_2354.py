# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.utils.timezone import utc
import datetime


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0011_auto_20151209_1633'),
    ]

    operations = [
        migrations.RenameField(
            model_name='event',
            old_name='dateEvent',
            new_name='dateEventStart',
        ),
        migrations.AddField(
            model_name='event',
            name='dateEventEnd',
            field=models.DateTimeField(default=datetime.datetime(2015, 12, 9, 23, 54, 39, 177578, tzinfo=utc)),
            preserve_default=False,
        ),
    ]
