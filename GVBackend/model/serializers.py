from model.models import GVUser, Event, Comment, Feed, Keyword, Tag
from django.contrib.auth.models import User
from rest_framework import serializers


class UserSerializer(serializers.Serializer):
    email = serializers.EmailField()
    username = serializers.CharField(max_length=100)

    class Meta:
        model = User
        fields = ('username','email')

class GVUserSerializer(serializers.ModelSerializer):

    attending = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='id'
    )

    maybes = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='id'
    )

    user = UserSerializer(required=False)

    class Meta:
        model = GVUser
        fields = ('id','user', 'name', 'bio', 'createdEvents',
                  'attendedEvents', 'attending', 'maybes')

    def update(self, instance, validated_data):

        try:
            instance.name = validated_data['name']
        except KeyError:
            pass
        try:
            instance.bio = validated_data['bio']
        except KeyError:
            pass

        instance.save()

        user = User.objects.get(gvuser=instance)
        try:
            user.username = user_data['username']
        except KeyError:
            pass
        try:
            user.email = user_data['email']
        except KeyError:
            pass
        user.save()

        return instance

class EventSerializer(serializers.ModelSerializer):

    attendees = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='id'
    )

    maybes = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='id'
    )

    eventTags = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='text'
    )

    eventKeywords = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='text'
    )

    dateCreated = serializers.DateTimeField(required=False)

    class Meta: 
        model = Event
        fields = ('id', 'name', 'description', 'exactLocation',
                  'displayLocation', 'dateEventStart','dateEventEnd',
                  'dateCreated', 'discoverable', 'host', 'past',
                  'attendees', 'maybes', 'eventTags', 'eventKeywords')

    def update(self, instance, validated_data):
        instance.name = validated_data['name']
        instance.description = validated_data['description']
        instance.exactLocation = validated_data['exactLocation']
        instance.displayLocation = validated_data['displayLocation']
        instance.dateEventStart = validated_data['dateEventStart']
        instance.dateEventEnd = validated_data['dateEventEnd']
        instance.discoverable = validated_data['discoverable']
        instance.past = validated_data['past']
        instance.save()

        return instance

class FeedSerializer(serializers.ModelSerializer):

    feedTags = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='text'
    )

    feedKeywords = serializers.SlugRelatedField(
        many=True,
        read_only=True,
        slug_field='text'
    )

    class Meta:
        model = Feed
        fields = ('id', 'name', 'owner', 'exactLocation', 'displayLocation',
                  'radius', 'startTime', 'endTime', 'feedKeywords', 'feedTags')

    def update(self, instance, validated_data):
        instance.name = validated_data['name']
        instance.exactLocation = validated_data['exactLocation']
        instance.displayLocation = validated_data['displayLocation']
        instance.radius = validated_data['radius']
        instance.startTime = validated_data['startTime']
        instance.endTime = validated_data['endTime']
        instance.save()

        return instance

class KeywordSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Keyword
        fields = ('text', )


class TagSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Tag
        fields = ('text', )


class CommentSerializer(serializers.HyperlinkedModelSerializer):

    author = GVUserSerializer()

    class Meta: 
        model = Comment
        fields = ('body', 'timeCreated', 'author')

