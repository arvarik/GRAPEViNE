from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import Tag
from model.serializers import TagSerializer


'''
    Views for the Tag model. Can create, update, delete tags.
'''

@api_view(['GET', 'POST'])
def GVTags(request):
    
    # Get all Tags
    if request.method == 'GET':
        tags = Tag.objects.all()
        serializer = TagSerializer(tags, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Create a Tag from the given request data
    elif request.method == 'POST':
        serializer = TagSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET', 'DELETE'])
def GVTagDetail(request,pk):
    
    # Checks to see if Tag exists
    try:
        tag = Tag.objects.get(id=pk)
    except Tag.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get a particular Tag given the request data
    if request.method == 'GET':
        serializer = TagSerializer(tag)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Delete a Tag
    elif request.method == 'DELETE':
        tag.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
