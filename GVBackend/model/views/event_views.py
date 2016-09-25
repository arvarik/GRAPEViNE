from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import GVUser, Event, Feed
from model.serializers import GVUserSerializer, EventSerializer
from django.contrib.auth.models import User


'''
    Views for the Event model. Can create, update, delete events as well
    as adding and removing users to an event depending if they are
    attending the event or if they are maybe attending the event.
'''

@api_view(['GET','POST'])
def GVEvents(request):
    
    # Get all Events
    if request.method == 'GET':
        events = Event.objects.all()
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data,status=status.HTTP_200_OK)
    
    # Create an Event from the given request data
    # Also checks to see if the associated GVUser exists
    elif request.method == 'POST':
        serializer = EventSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            event = Event.objects.get(id=serializer.data['id'])
            hostID = request.data['host']
            try:
                host = GVUser.objects.get(id=hostID)
            except GVUser.DoesNotExist:
                event.delete()
                return Response(status=status.HTTP_404_NOT_FOUND)
            
            host.host.add(event)
            serializer = EventSerializer(event)
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'POST', 'DELETE'])
def GVEventDetail(request,pk):
    
    # Checks to see if Event exists
    try:
        event = Event.objects.get(id=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get Event
    if request.method == 'GET':
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Updates an Event from the given request data
    elif request.method == 'POST':
        serializer = EventSerializer(event, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    # Delete an Event
    elif request.method == 'DELETE':
        event.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)



@api_view(['POST', 'DELETE'])
def GVEventAttending(request,pk):
    
    # Checks to see if Event exists
    try:
        event = Event.objects.get(id=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Checks to see if the GVUser exists
    userID = request.data['userID']
    try:
        gvuser = GVUser.objects.get(id=userID)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Add user attending an Event
    if request.method == 'POST':
        
        event.attendees.add(gvuser)
        # GVUser can't attend and maybe the same event
        if gvuser in event.maybes.all():
            event.maybes.remove(gvuser)
        
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Remove user attending an Event
    elif request.method == 'DELETE':
        
        event.attendees.remove(gvuser)
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)

@api_view(['POST'])
def GVEventNotAttending(request,pk):
    
    # Checks to see if Event exists
    try:
        event = Event.objects.get(id=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Checks to see if the GVUser exists
    userID = request.data['userID']
    try:
        gvuser = GVUser.objects.get(id=userID)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Remove user attending an Event
    if request.method == 'POST':
        
        event.attendees.remove(gvuser)
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)


@api_view(['POST', 'DELETE'])
def GVEventMaybe(request,pk):
    
    # Checks to see if Event exists
    try:
        event = Event.objects.get(id=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Checks to see if the GVUser exists
    userID = request.data['userID']
    try:
        gvuser = GVUser.objects.get(id=userID)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Add user attending an Event
    if request.method == 'POST':
        
        event.maybes.add(gvuser)
        if gvuser in event.maybes.all():
            event.attendees.remove(gvuser)
        
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)

    # Remove user attending an Event
    elif request.method == 'POST':
        
        event.maybes.remove(gvuser)
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)


@api_view(['POST'])
def GVEventNotMaybe(request,pk):
    
    # Checks to see if Event exists
    try:
        event = Event.objects.get(id=pk)
    except Event.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Checks to see if the GVUser exists
    userID = request.data['userID']
    try:
        gvuser = GVUser.objects.get(id=userID)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Remove user attending an Event
    if request.method == 'POST':
        
        event.maybes.remove(gvuser)
        serializer = EventSerializer(event)
        return Response(serializer.data, status=status.HTTP_200_OK)
