from django.db import models
from django.contrib.auth.models import User
 

class GVUser(models.Model):
    user = models.OneToOneField(User, null=True)
    bio = models.TextField(blank=True, null=True)
    name = models.CharField(max_length=40, blank=True, null=True)
    createdEvents = models.IntegerField(blank=True, null=True)
    attendedEvents = models.IntegerField(blank=True, null=True) 
    attending = models.ManyToManyField('Event', related_name='attendees',blank=True)
    maybes = models.ManyToManyField('Event', related_name='maybes', blank=True)

    def __str__(self):
        return self.user.username


class Comment(models.Model):
    body = models.TextField()
    timeCreated = models.DateTimeField(auto_now=True)
    author = models.ForeignKey('GVUser', related_name='author')
    event = models.ForeignKey('Event', related_name='comments')


class Keyword(models.Model):
    text = models.CharField(max_length=40)
    eventKeywords = models.ManyToManyField('Event', related_name='eventKeywords')
    feedKeywords = models.ManyToManyField('Feed', related_name='feedKeywords')

    def __str__(self):
        return self.text


class Tag(models.Model):
    text = models.CharField(max_length=40)
    eventTags = models.ManyToManyField('Event', related_name='eventTags')
    feedTags = models.ManyToManyField('Feed', related_name='feedTags')

    def __str__(self):
        return self.text


class Event(models.Model):
    name = models.CharField(max_length=60)
    description = models.TextField()
    exactLocation = models.CharField(max_length=80)
    displayLocation = models.CharField(max_length=60)
    dateEventStart = models.DateTimeField(auto_now=False)
    dateEventEnd = models.DateTimeField(auto_now=False)
    dateCreated = models.DateTimeField(auto_now=False)
    discoverable = models.BooleanField()
    host = models.ForeignKey('GVUser', related_name='host', null=True)
    past = models.BooleanField()

   
    def __str__(self): 
        return self.name


class Feed(models.Model):
    owner = models.ForeignKey('GVUser', related_name='feeds', null=True)
    name = models.CharField(max_length=60)
    exactLocation = models.CharField(max_length=80)
    displayLocation = models.CharField(max_length=80)
    radius = models.FloatField()
    startTime = models.TimeField(auto_now=False)
    endTime = models.TimeField(auto_now=False)

    def __str__(self): 
        return self.name
