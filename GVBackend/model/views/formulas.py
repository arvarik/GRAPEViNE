#!/usr/bin/env python

# Haversine formula example in Python
# Author: Wayne Dyck
# https://gist.github.com/rochacbruno/2883505#file-haversine-py
from model.models import Event
import math
from datetime import datetime

def distance(origin, destination):
    lat1, lon1 = origin
    lat2, lon2 = destination
    radius = 3959 # mi

    dlat = math.radians(lat2-lat1)
    dlon = math.radians(lon2-lon1)
    a = math.sin(dlat/2) * math.sin(dlat/2) + math.cos(math.radians(lat1)) \
        * math.cos(math.radians(lat2)) * math.sin(dlon/2) * math.sin(dlon/2)
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    d = radius * c

    return d

# Delete past Events
def deletePastEvents():
    Event.objects.filter(dateEventEnd__lt=datetime.now()).delete()  