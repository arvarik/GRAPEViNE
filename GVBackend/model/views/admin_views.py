from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import GVUser, Event, Comment, Feed, Keyword, Tag
from model.serializers import GVUserSerializer, EventSerializer, KeywordSerializer
from model.serializers import CommentSerializer, FeedSerializer, TagSerializer

''' 
These methods fetch all the rows of data in the respective model table.

The viewsets are only used for the admin page for testing purposes. 
'''

class GVUserViewSet(viewsets.ModelViewSet):
    queryset = GVUser.objects.all()
    serializer_class = GVUserSerializer


class EventViewSet(viewsets.ModelViewSet):
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class CommentViewSet(viewsets.ModelViewSet):
    queryset = Comment.objects.all()
    serializer_class = CommentSerializer


class FeedViewSet(viewsets.ModelViewSet):
    queryset = Feed.objects.all()
    serializer_class = FeedSerializer


class KeywordSet(viewsets.ModelViewSet):
    queryset = Keyword.objects.all()
    serializer_class = KeywordSerializer


class TagSet(viewsets.ModelViewSet):
    queryset = Tag.objects.all()
    serializer_class = TagSerializer

