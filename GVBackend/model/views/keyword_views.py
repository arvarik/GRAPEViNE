from django.shortcuts import render
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import api_view
from rest_framework.response import Response
from model.models import Keyword
from model.serializers import KeywordSerializer


'''
    Views for the Keyword model. Can create, update, delete keywords.
'''

@api_view(['GET', 'POST'])
def GVKeywords(request):
    
    # Get all Keywords
    if request.method == 'GET':
        keywords = Keyword.objects.all()
        serializer = KeywordSerializer(keywords, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Create a Keyword from given request data
    elif request.method == 'POST':
        serializer = KeywordSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        else:
            return Response(status=status.HTTP_400_BAD_REQUEST)


@api_view(['GET',  'DELETE'])
def GVKeywordDetail(request,pk):
    
    # Checks to see if Keyword exists
    try:
        keyword = Keyword.objects.get(id=pk)
    except Keyword.DoesNotExist:
        return Response(status=status.HTTP_404_NOT_FOUND)
    
    # Get a particular Keyword from given request data
    if request.method == 'GET':
        serializer = KeywordSerializer(keyword)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    # Delete a Keyword
    elif request.method == 'DELETE':
        keyword.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
