"""GVBackend URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import include, url
from django.contrib import admin
from rest_framework import routers
from model import views

router = routers.DefaultRouter()

# Makes sure that the API endpoints work
router.register(r'api/model/gvusers', views.GVUserViewSet)
router.register(r'api/model/events', views.EventViewSet)
router.register(r'api/model/comments', views.CommentViewSet)
router.register(r'api/model/feeds', views.FeedViewSet)
router.register(r'api/model/keyword', views.KeywordSet)
router.register(r'api/model/tags', views.TagSet)

admin.autodiscover()

urlpatterns = [

    url(r'^admin/', include(admin.site.urls)),
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),

    url(r'^users/$', views.GVUsers),
    url(r'^users/([0-9]+)/$', views.GVUserDetail),
    url(r'^users/events/([0-9]+)/$', views.GVUserEvents),
    url(r'^users/feeds/([0-9]+)/$', views.GVUserFeeds),
    url(r'^users/changeemail/([0-9]+)/$', views.GVChangeEmail),
    url(r'^users/changepassword/([0-9]+)/$', views.GVChangePassword),
    url(r'^users/changebio/([0-9]+)/$', views.GVChangeBio),

    url(r'^events/$', views.GVEvents),
    url(r'^events/([0-9]+)/$', views.GVEventDetail),
    url(r'^events/attending/([0-9]+)/$', views.GVEventAttending),
    url(r'^events/maybe/([0-9]+)/$', views.GVEventMaybe),
    url(r'^events/notattending/([0-9]+)/$', views.GVEventNotAttending),
    url(r'^events/notmaybe/([0-9]+)/$', views.GVEventNotMaybe),

    url(r'^feeds/$', views.GVFeeds),
    url(r'^feeds/([0-9]+)/$', views.GVFeedDetail),
    url(r'^feeds/events/([0-9]+)/$', views.GVFeedEvents),

    url(r'^tags/$', views.GVTags),
    url(r'^tags/([0-9]+)/$', views.GVTagDetail),

    url(r'^keywords/$', views.GVKeywords),
    url(r'^keywords/([0-9]+)/$', views.GVKeywordDetail),


    url(r'^search/$', views.GVSearch),

]
