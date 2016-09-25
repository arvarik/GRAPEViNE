from django.test import TestCase
from model.models import GVUser, Event, Feed, Keyword, Tag
from model.views import *
from django.contrib.auth.models import User
from rest_framework import status
from rest_framework.test import APITestCase


'''
Tests the views methods. Perform tests with the following command in
the app folder:

.python3 manage.py test tests/


Througout the development of our backend we have been testing the views using
Postman.
'''


## Tests POST Requests
class ModelPostTestCase(TestCase):
    
    # Tests if the post request works for gvuser creation
    def test_gvuser_creation(self):
        
        url = '/users/'
        data = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        
        self.assertEqual(GVUser.objects.count(), 1)
        self.assertEqual(GVUser.objects.get(id=1).__str__(), 'Tom')


    # Tests if the post request works for event creation
    def test_event_creation(self):
        
        # Creating a gvuser to test event
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/events/'
        data = {'name':'LinAlg','description':'Linear Algebra','exactLocation':'Coordinates',
                'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                'discoverable':'true','host':1,'past':'false'}
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Event.objects.count(), 1)
        self.assertEqual(Event.objects.get(id=1).__str__(), 'LinAlg')


    # Tests if the post request works for feed creation
    def test_feed_creation(self):
        
        # Creating a gvuser to test feed
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/feeds/'
        data = {'name':'Calc','ownerID':1,'exactLocation':'Coordinates',
                'displayLocation':'Brody','radius':5.0,
                'startTime':'19:34:30','endTime':'20:34:30'}
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Feed.objects.count(), 1)
        self.assertEqual(Feed.objects.get(id=1).__str__(), 'Calc')


    # Tests if the post request works for tag creation
    def test_tag_creation(self):
        
        url = '/tags/'
        data = {'text':'FirstTag'}
        response = self.client.post(url, data, format='json')
        
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertEqual(Tag.objects.count(), 1)
        self.assertEqual(Tag.objects.get(id=1).__str__(), 'FirstTag')


## Tests GET Requests
class ModelGetTestCase(TestCase):
    
    # Tests if the get request works for gvusers
    def test_gvuser_get(self):
        
        url = '/users/'
        data = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url, data, format='json')

        response = self.client.get('/users/1/')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        
        # Checks to see if the get returns the single instance just created
        user = response.data['user']
        self.assertEqual(response.data['id'], 1)
        self.assertEqual(user['username'], 'Tom')


    # Tests if the get request works for feeds
    def test_feed_get(self):
    
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/feeds/'
        data = {'name':'Calc','ownerID':1,'exactLocation':'Coordinates',
                'displayLocation':'Brody','radius':5.0,
                'startTime':'19:34:30','endTime':'20:34:30'}
        self.client.post(url, data, format='json')
        
        # Checks to see if the get returns the single instance just created
        response = self.client.get('/feeds/1/')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['id'], 1)
        self.assertEqual(response.data['name'], 'Calc')


    # Tests if the get request works for events
    def test_event_get(self):
    
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/events/'
        data = {'name':'LinAlg','description':'Linear Algebra','exactLocation':'Coordinates',
                'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                'discoverable':'true','host':1,'past':'false'}
        self.client.post(url, data, format='json')
        
        # Checks to see if the get returns the single instance just created
        response = self.client.get('/events/1/')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['id'], 1)
        self.assertEqual(response.data['name'], 'LinAlg')


## Tests DELETE Requests
class ModelDeleteTestCase(TestCase):

    # Tests if the delete request works for gvusers
    def test_gvuser_deletion(self):
    
        url = '/users/'
        data = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url, data, format='json')

        # Checks to see if the object was deleted
        response = self.client.delete('/users/1/')
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)


    # Tests if the delete request works for events
    def test_event_deletion(self):
    
        # Creating a gvuser for event
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/events/'
        data = {'name':'LinAlg','description':'Linear Algebra','exactLocation':'Coordinates',
                'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                'discoverable':'true','host':1,'past':'false'}
        self.client.post(url, data, format='json')
        
        # Checks to see if the object was deleted
        response = self.client.delete('/events/1/')
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

    # Tests if the delete request works for feeds
    def test_feed_deletion(self):
    
        # Creating a gvuser for feed
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        url = '/feeds/'
        data = {'name':'Calc','ownerID':1,'exactLocation':'Coordinates',
                'displayLocation':'Brody','radius':5.0,
                'startTime':'19:34:30','endTime':'20:34:30'}
        self.client.post(url, data, format='json')
        
        # Checks to see if the object was deleted
        response = self.client.delete('/feeds/1/')
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)

    # Tests if the delete request works for gvusers
    def test_tag_deletion(self):
    
        url = '/tags/'
        data = {'text':'FirstTag'}
        self.client.post(url, data, format='json')
        
        # Checks to see if the object was deleted
        response = self.client.delete('/tags/1/')
        self.assertEqual(response.status_code, status.HTTP_204_NO_CONTENT)


## Tests Search function
class ModelExtraTestCase(TestCase):

    def test_event_search(self):
        # Creating a gvuser for event
        url_host = '/users/'
        data_host = {'email':'Tom@yahoo.com','username':'Tom','password':'hunter2'}
        self.client.post(url_host, data_host, format='json')
        
        # Creating events to query from
        url_event = '/events/'
        event_1 = {'name':'LinAlg','description':'Linear Algebra','exactLocation':'Coordinates',
                   'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                   'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                   'discoverable':'true','host':1,'past':'false'}
        event_2 = {'name':'Calc is fun','description':'Calc','exactLocation':'Coordinates',
                   'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                   'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                   'discoverable':'true','host':1,'past':'false'}
        event_3 = {'name':'English','description':'English','exactLocation':'Coordinates',
                   'displayLocation':'Brody','dateEventStart':'2015-12-03T19:34:30Z',
                   'dateEventEnd':'2015-12-03T19:34:30Z','dateCreated':'2015-12-03T19:34:30Z',
                   'discoverable':'true','host':1,'past':'false'}
                   
        self.client.post(url_event, event_1, format='json')
        self.client.post(url_event, event_2, format='json')
        self.client.post(url_event, event_3, format='json')
        
        # Search query for finding events with the word 'fun' in the name
        url = '/search/'
        data_query = {'query': 'fun'}
        response = self.client.post(url, data_query, format='json')
        data = response.data[0]

        self.assertEqual(data['id'], 2)
        self.assertEqual(data['name'], 'Calc is fun')

        # Search query for finding events by its inherent id
        data_query = {'id': '3'}
        response = self.client.post(url, data_query, format='json')
        data = response.data[0]
        
        self.assertEqual(data['id'], 3)
        self.assertEqual(data['name'], 'English')

