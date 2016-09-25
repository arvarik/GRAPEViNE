from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import GVUser, Event, Feed
from model.serializers import GVUserSerializer, EventSerializer
from django.utils.datastructures import MultiValueDictKeyError
from django.contrib.auth.models import User
from . import formulas
'''
   Search method to search an event by its title or id.
   Also includes helper methods to assist methods in other view files.
'''


@api_view(['POST'])
def GVSearch(request):
    ''' Search Method
        
        Search for events with a POST request in order to use a query
        for text or id to search for the events.
        
        Returns the event(s) that have the specification requested.
    '''
    if request.method == 'POST':
        try:
            query = request.data['query']
            queryset = Event.objects.filter(name__icontains=query)
            serializer = EventSerializer(queryset,many=True)
            return Response(serializer.data,status=status.HTTP_200_OK)
        except MultiValueDictKeyError:
            try:
                id = request.data['id']
                queryset = Event.objects.filter(id=id)
                serializer = EventSerializer(queryset, many=True)
                return Response(serializer.data,status=status.HTTP_200_OK)
            except MultiValueDictKeyError:
                return Response(status=status.HTTP_400_BAD_REQUEST)


def username_is_valid(username):
    ''' Helper Method (For GVUSer)
        
        Checks if the username is already taken.
        
        Returns False if the username is taken, else return True.
    '''
    try:
        User.objects.get(username=username)
        return False
    except User.DoesNotExist:
        return True

def getTimelyEvents(events,st,et):
    ''' Helper Method (For Feed)
        
        Finds all the events within a specific time period given a list 
        of events and a start and end time.
        
        Returns a list of events that are within the given time period.
    '''
    timely = []
    for event in events:
        if event.dateEventStart.time() >= st:
            if event.dateEventEnd.time() <= et:
                timely.append(event)

    return timely


def getNearbyEvents(events,c,r):
    ''' Helper Method (For Feed)
        
        Givena list of events, this method finds all events within a
        specific radius of the given coordinates.
        Uses the Haversine formula to calculate distance.
        
        Returns a list of events that are within the given radius.
    '''
    nearby = []
    x1,y1 = coordinates(c)
    for event in events:
        x2,y2 = coordinates(event.exactLocation)
        x1 = float(x1)
        y1 = float(y1)
        x2 = float(x2)
        y2 = float(y2)
        r = float(r)
        dist = formulas.distance([x1,y1],[x2,y2])
        if dist < r:
            nearby.append(event)

    return nearby


def coordinates(c):
    ''' Helper Method (For Feed)
        
        Parses a given string containing exact location of an event.
        
        Returns a list of two strings containing the x and y 
        coordinates.
    '''
    c = c[1:len(c)-1]
    return c.split(',')  

