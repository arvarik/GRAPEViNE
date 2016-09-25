from django.contrib import admin
from model.models import GVUser, Event, Comment, Feed, Keyword, Tag


class GVUserAdmin(admin.ModelAdmin):
    list_display = ('name', 'user')


class EventAdmin(admin.ModelAdmin):
    list_display = ('name', 'dateCreated', 'dateEventStart', 'dateEventEnd',
                    'host', 'past')


class CommentAdmin(admin.ModelAdmin):
    list_display = ('body', 'timeCreated', 'author')


class FeedAdmin(admin.ModelAdmin):
    list_display = ('name', 'displayLocation')


class KeywordAdmin(admin.ModelAdmin):
    list_display = ('text',)


class TagAdmin(admin.ModelAdmin):
    list_display = ('text',)

# Register models here.
admin.site.register(GVUser, GVUserAdmin)
admin.site.register(Event, EventAdmin)
admin.site.register(Comment, CommentAdmin)
admin.site.register(Feed, FeedAdmin)
admin.site.register(Keyword, KeywordAdmin)
admin.site.register(Tag, TagAdmin)
