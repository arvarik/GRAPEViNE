from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import GVUser, Event, Feed, Keyword, Tag
from model.serializers import GVUserSerializer, EventSerializer, FeedSerializer
from django.contrib.auth.models import User
from . import formulas

from .extra_views import *

'''
    Views for the Feed model. Can create, update, delete feeds as well
    as retrieving and accessing all the associated events with the
    feed based on its specific parameters.
    
    Also utilizes the our nearby algorithm to find nearby events based 
    on the Haversine equation.
'''


@api_view(['GET', 'POST'])
def GVFeeds(request):
    
    # Get all Feeds
    if request.method == 'GET':
        feeds = Feed.objects.all()
        serializer = FeedSerializer(feeds, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Create a Feed from the request data
    # Also checks to see if the GVUser exists
    elif request.method == 'POST':
        serializer = FeedSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            feed = Feed.objects.get(id=serializer.data['id'])
            
            ownerID = request.data['owner']
            try:
                owner = GVUser.objects.get(id=ownerID)
            except GVUser.DoesNotExist:
                feed.delete()
                return Response(status=status.HTTP_404_NOT_FOUND)
            
            owner.feeds.add(feed)
            serializer = FeedSerializer(feed)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'POST', 'DELETE'])
def GVFeedDetail(request,pk):
    
    # Checks to see if Feed exists
    try:
        feed = Feed.objects.get(id=pk)
    except Feed.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get a particular Feed from request data
    if request.method == 'GET':
        serializer = FeedSerializer(feed)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    ## Updates a Feed given new Feed parameters
    if request.method == 'POST':
        serializer = FeedSerializer(feed, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    # Delete a Feed
    if request.method == 'DELETE':
        feed.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


@api_view(['GET'])
def GVFeedEvents(request,pk):
    
    # Checks to see if Feed exists
    try:
        feed = Feed.objects.get(id=pk)
    except Feed.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Retrieve all the events pertaining to a feed
    if request.method == 'GET':

        formulas.deletePastEvents()
        
        events = Event.objects.all()
        events = getTimelyEvents(events,feed.startTime,feed.endTime)
        events = getNearbyEvents(events,feed.exactLocation,feed.radius)
        serializer = EventSerializer(events, many=True)
        
        return Response(serializer.data, status=status.HTTP_200_OK)

