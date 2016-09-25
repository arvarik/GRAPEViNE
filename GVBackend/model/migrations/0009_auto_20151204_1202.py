# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0008_auto_20151204_0038'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='gvuser',
            name='id',
        ),
        migrations.AlterField(
            model_name='gvuser',
            name='user',
            field=models.OneToOneField(serialize=False, primary_key=True, to=settings.AUTH_USER_MODEL, default=0),
            preserve_default=False,
        ),
    ]
