# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0009_auto_20151204_1202'),
    ]

    operations = [
        migrations.AddField(
            model_name='gvuser',
            name='id',
            field=models.AutoField(primary_key=True, default=1, auto_created=True, serialize=False, verbose_name='ID'),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='gvuser',
            name='user',
            field=models.OneToOneField(to=settings.AUTH_USER_MODEL),
        ),
    ]
