from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import GVUser, Event, Comment, Feed, Keyword, Tag
from model.serializers import GVUserSerializer, EventSerializer, KeywordSerializer
from model.serializers import CommentSerializer, FeedSerializer, TagSerializer
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login
from django.utils.datastructures import MultiValueDictKeyError
from .extra_views import *

'''
    Views for the GVuser model. Can create, update, delete gvusers
    as well as retrieve and access the associated events that the
    user has created and the feeds that the user has made.
    
    Also includes user authentication to check login credentials.
'''

@api_view(['GET','POST','PUT'])
def GVUsers(request):
    
    # Get all GVUsers
    if request.method == 'GET':
        
        gvusers = GVUser.objects.all()
        serializer = GVUserSerializer(gvusers, many=True)
        return Response(serializer.data,status=status.HTTP_200_OK)
    
    # Create a new GVUser. Checks if the username is unique, if it is, create new user.
    elif request.method == 'POST':
        username = request.data['username']
        if username_is_valid(username):
            email = request.data['email']
            password = request.data['password']
            user = User.objects.create_user(username=username, email=email, password=password)
            user.save()
            gvuser = GVUser(user=user,)
            gvuser.save()
            serializer = GVUserSerializer(gvuser)
            return Response(serializer.data,status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)

    # Login GVUser. Authenticate GVUser using username and password.
    elif request.method == 'PUT':
        username = request.data['username']
        password = request.data['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            gvuser = GVUser.objects.get(user=user)
            serializer = GVUserSerializer(gvuser)
            return Response(serializer.data,status=status.HTTP_200_OK)
        else:
            return Response(status=status.HTTP_401_UNAUTHORIZED)


@api_view(['GET','PUT','DELETE'])
def GVUserDetail(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get GVUser if it exists.
    if request.method == 'GET':
        serializer = GVUserSerializer(gvuser)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    
    # Update GVUser if it exists
    elif request.method == 'PUT':
        serializer = GVUserSerializer(gvuser, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    # Delete a GVUser
    elif request.method == 'DELETE':
        gvuser.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

@api_view(['GET'])
def GVUserEvents(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get GVUser's created events
    if request.method == 'GET':
        createdevents = Event.objects.filter(host=gvuser)
        attendingevents = gvuser.attending
        maybeevents = gvuser.maybes
        serializer1 = EventSerializer(createdevents,many=True)
        serializer2 = EventSerializer(attendingevents,many=True)
        serializer3 = EventSerializer(maybeevents,many=True)
        data = serializer1.data + serializer2.data + serializer3.data
        return Response(data, status=status.HTTP_200_OK)

@api_view(['GET'])
def GVUserFeeds(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get GVUser's feeds
    if request.method == 'GET':
        feeds = Feed.objects.filter(owner=gvuser)
        serializer = FeedSerializer(feeds,many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)

@api_view(['POST'])
def GVChangeEmail(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Change user email
    if request.method == 'POST':
        user = gvuser.user
        email = request.data['email']
        user.email = email
        user.save()
        gvuser_new = GVUser.objects.get(user=user)
        serializer = GVUserSerializer(gvuser_new)
        return Response(serializer.data, status=status.HTTP_202_ACCEPTED)

@api_view(['POST'])
def GVChangePassword(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Change user password
    if request.method == 'POST':
        user = authenticate(username=request.data['username'], password=request.data['oldPassword'])
        if user is not None:
            new_password = request.data['password']
            user.set_password(new_password)
            user.save()
            gvuser_new = GVUser.objects.get(user=user)
            serializer = GVUserSerializer(gvuser_new)
            return Response(serializer.data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(status=status.HTTP_404_NOT_FOUND)


@api_view(['POST'])
def GVChangeBio(request,pk):
    
    # Check to see if the user exists
    try:
        gvuser = GVUser.objects.get(id=pk)
    except GVUser.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)

    # Change user bio
    if request.method == 'POST':
        gvuser.bio = request.data['bio']
        gvuser.save()
        serializer = GVUserSerializer(gvuser)
        return Response(serializer.data, status=status.HTTP_202_ACCEPTED)