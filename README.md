Durian Extractor
=

Web page extractor and readability using Jsoup. Enable javascript serverside rendering 
support using JBrowserDriver (Selenium).

Prerequisites:
-
*	Java JDK-1.8 or higher
*	Apache Maven 3 or higher
*	Please refer http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html for any help in Maven.


Install
-

because this project not pushed to any public maven repos, you should install it first locally

        mvn clean install

add this project as dependency of your project

	    <dependency>
            <groupId>co.mailtarget</groupId>
            <artifactId>durian</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        
Usage
-

###kotin
        
        val extractor = WebExtractor.Builder
                        .strategy(Strategy.HYBRID)
                        .build()
        
        val webData = extractor.extract(url)
        
or 
        
        val forceJavascript = false
        WebData webData = extractor.extract(url, forceJavacript)

###Java
        
        WebExtractor extractor = WebExtractor.Builder()
                        .strategy(Strategy.HYBRID)
                        .build();
        WebData webData = extractor.extract(url);

or 
        
        boolean forceJavascript = false;
        WebData webData = extractor.extract(url, forceJavacript);
        
        
Options
-

###Extract Strategy 

- **META** : fastest method, just parse content from meta
- **CONTENT** : prefer using content as source of extraction
- **HYBRID** : fetch from meta first, if not found search deeper from content

###System Config

tried in MAC OS machine and work well, on centos machine, please install

        yum groupinstall -y "Fonts"
        yum install gtk2 

> optional : gtkhtml3 libXtst libxslt alsa-lib 
