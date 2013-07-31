internationumber
================

Internationumber.com

How to build
====================
Building is pretty much straight-forward, apart from a few minor things:

1. You'll need to create your own file called *GooglePlayDeveloper.java* in the package *com.ninetwozero.internationumber*. See the code below for more information:

````
/*
	This file is part of Internationumber

    Internationumber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Internationumber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.ninetwozero.internationumber;

public class GooglePlayDeveloper {
	public static final String PLAY_API_KEY = "<YOUR API KEY HERE>";
	private GooglePlayDeveloper() {}
}
````