from django.test import TestCase
from model.models import GVUser, Event, Comment, Feed, Keyword, Tag
from django.contrib.auth.models import User

''' 
Tests for model creation. Perform tests with the following command in app folder:

python3 manage.py test tests/
'''


class GVUserTestCase(TestCase):
    def test_gvuser(self):
        # Tests to see is GVUser creation is working 

        # Preliminary models needed to be created for testing
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')

        # Model that is being tested 
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')
        
        # Tests data of the GVUser to see if it is being indexed correctly and has the
        # correct name
        self.assertEqual(str(firstGVUser.user), 'arvind')
        self.assertEqual(firstGVUser.pk, 1)
        self.assertEqual(firstGVUser.__str__(), 'arvind')

    def test_gvuser_attending(self):
        # Tests to see if attending/maybe events list for a GVUser is working 

        # Preliminary models needed to be created for testing 
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')
        # Model that is being tested 
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')
        firstEvent = Event.objects.create(name='Calc study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True, host=firstGVUser, past=False)
        secondEvent = Event.objects.create(name='English study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True, host=firstGVUser, past=False)

        firstGVUser.save()
        firstGVUser.attending.add(firstEvent)
        firstGVUser.maybes.add(secondEvent)
        
        # Tests if events can be added to a user for their attending and maybe lists.
        # Checks if the correct event is added to each list
        self.assertEquals(str(firstGVUser.attending.all()[0]), 'Calc study')
        self.assertEquals(str(firstGVUser.maybes.all()[0]), 'English study')


class EventTestCase(TestCase):
    def test_event(self):
        #  Tests to see if Event creation is working 

        #  Preliminary models needed to be created for testing 
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')

        #  Model that is being tested
        firstEvent = Event.objects.create(name='Calc study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True, host=firstGVUser, past=False)

        # Tests that all the data is properly inputted, and also foreign keys
        # Checks if the created event is indexed properly by primary pk
        self.assertEquals(firstEvent.__str__(), 'Calc study')
        self.assertEquals(firstEvent.pk, 1)
        self.assertEquals(firstEvent.exactLocation, 'Coordinates')
        self.assertEquals(str(firstEvent.host), 'arvind')


class FeedTestCase(TestCase):
    def test_feed(self):
        # Tests to see if Feed creation is working

        # Preliminary models needed to be created for testing
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')

        # Model that is being tested
        firstFeed = Feed.objects.create(owner=firstGVUser, name='Linear Algebra',
                                        exactLocation='Coordinates', displayLocation='Brody',
                                        radius=2.5, startTime='08:30', endTime='09:30')
                                        
        # Tests that the feed is created properly with with the given data
        # Checks the foreign key and that it is indexed correctly with the primary key
        self.assertEquals(firstFeed.__str__(), 'Linear Algebra')
        self.assertEquals(firstFeed.pk, 1)
        self.assertEquals(firstFeed.radius, 2.5)
        self.assertEquals(str(firstFeed.owner), 'arvind')



class CommentTestCase(TestCase):
    def test_comment(self):
        # Tests to see if Comment creation is working

        # Preliminary models needed to be created for testing
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')
        firstEvent = Event.objects.create(name='Calc study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True,
                                          host=firstGVUser, past=False)

        # Model that is being tested
        firstComment = Comment.objects.create(body='First Comment', author=firstGVUser,
                                              event=firstEvent)

        # Tests that the comment is correctly created
        self.assertEquals(firstComment.pk, 1)
        self.assertEquals(firstComment.body, 'First Comment')
        self.assertEquals(str(firstComment.author), 'arvind')
        self.assertEquals(str(firstComment.event), 'Calc study')


class TagTestCase(TestCase):
    def test_tag(self):
        # Tests to see if tag creation is working. Also tests if Keyword creation is working
        # because the two models essentially are the same
        
        # Preliminary models needed to be created for testing
        firstUser = User.objects.create_user(username='arvind', email='arvind@yahoo.com',
                                             password='hunter2')
        firstGVUser = GVUser.objects.create(user=firstUser, bio='My name is arvind',
                                            name='Arvind Arikatla')
        firstEvent = Event.objects.create(name='Calc study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True, host=firstGVUser, past=False)
        secondEvent = Event.objects.create(name='English study', description='Exam review',
                                          exactLocation='Coordinates', displayLocation='Brody',
                                          dateEventStart='2015-12-22T08:30',
                                          dateEventEnd='2015-12-22T08:30',
                                          dateCreated='2015-12-12T08:30',
                                          discoverable=True, host=firstGVUser, past=False)
        firstFeed = Feed.objects.create(owner=firstGVUser, name='Linear Algebra',
                                        exactLocation='Coordinates', displayLocation='Brody',
                                        radius=2.5, startTime='08:30', endTime='09:30')

        # Model that is being tested
        firstTag = Tag.objects.create(text='Calculus')
        firstTag.save()
        firstTag.eventTags.add(firstEvent, secondEvent)
        firstTag.feedTags.add(firstFeed)
        
        # Tests if tag creation works by checking its data and primary key
        # Also checks if it can be correctly added to feeds and events
        self.assertEquals(firstTag.pk, 1)
        self.assertEquals(firstTag.text, 'Calculus')
        self.assertEquals(str(firstTag.eventTags.all()[1]), 'English study')
        self.assertEquals(str(firstTag.feedTags.all()[0]), 'Linear Algebra')
