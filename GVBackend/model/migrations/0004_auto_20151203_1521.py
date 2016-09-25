# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('model', '0003_auto_20151203_1517'),
    ]

    operations = [
        migrations.AlterField(
            model_name='event',
            name='host',
            field=models.ForeignKey(blank=True, null=True, related_name='event_host', to='model.GVUser'),
        ),
    ]
