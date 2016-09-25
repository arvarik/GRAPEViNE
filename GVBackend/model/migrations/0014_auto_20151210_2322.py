# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0013_auto_20151210_0145'),
    ]

    operations = [
        migrations.AlterField(
            model_name='feed',
            name='owner',
            field=models.ForeignKey(to='model.GVUser', related_name='feeds', null=True),
        ),
    ]
