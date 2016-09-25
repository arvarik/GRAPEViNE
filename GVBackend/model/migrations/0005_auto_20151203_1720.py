# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0004_auto_20151203_1521'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='host',
            field=models.ForeignKey(to='model.GVUser', default=1, related_name='event_host'),
            preserve_default=False,
        ),
    ]
