# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Comment',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('body', models.TextField()),
                ('timeCreated', models.DateTimeField(auto_now=True)),
            ],
        ),
        migrations.CreateModel(
            name='Event',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('name', models.CharField(max_length=60)),
                ('description', models.TextField()),
                ('exactLocation', models.CharField(max_length=80)),
                ('displayLocation', models.CharField(max_length=60)),
                ('dateEvent', models.DateTimeField()),
                ('dateCreated', models.DateTimeField(auto_now=True)),
                ('discoverable', models.BooleanField()),
                ('past', models.BooleanField()),
            ],
        ),
        migrations.CreateModel(
            name='Feed',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('title', models.CharField(max_length=60)),
                ('exactLocation', models.CharField(max_length=80)),
                ('displayLocation', models.CharField(max_length=80)),
                ('radius', models.FloatField()),
                ('startTime', models.TimeField()),
                ('endTime', models.TimeField()),
            ],
        ),
        migrations.CreateModel(
            name='GVUser',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('bio', models.TextField(null=True, blank=True)),
                ('name', models.CharField(null=True, blank=True, max_length=40)),
                ('createdEvents', models.IntegerField(null=True, blank=True)),
                ('attendedEvents', models.IntegerField(null=True, blank=True)),
                ('attending', models.ManyToManyField(blank=True, related_name='attendees', to='model.Event')),
                ('maybes', models.ManyToManyField(blank=True, related_name='maybes', to='model.Event')),
                ('user', models.OneToOneField(to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.CreateModel(
            name='Keyword',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('text', models.CharField(max_length=40)),
                ('eventKeywords', models.ManyToManyField(related_name='eventKeywords', to='model.Event')),
                ('feedKeywords', models.ManyToManyField(related_name='feedKeywords', to='model.Feed')),
            ],
        ),
        migrations.CreateModel(
            name='Tag',
            fields=[
                ('id', models.AutoField(serialize=False, primary_key=True, auto_created=True, verbose_name='ID')),
                ('text', models.CharField(max_length=40)),
                ('eventTags', models.ManyToManyField(related_name='eventTags', to='model.Event')),
                ('feedTags', models.ManyToManyField(related_name='feedTags', to='model.Feed')),
            ],
        ),
        migrations.AddField(
            model_name='feed',
            name='owner',
            field=models.ForeignKey(related_name='feeds', to='model.GVUser'),
        ),
        migrations.AddField(
            model_name='event',
            name='host',
            field=models.ForeignKey(related_name='event_host', to='model.GVUser'),
        ),
        migrations.AddField(
            model_name='comment',
            name='author',
            field=models.ForeignKey(related_name='author', to='model.GVUser'),
        ),
        migrations.AddField(
            model_name='comment',
            name='event',
            field=models.ForeignKey(related_name='comments', to='model.Event'),
        ),
    ]
