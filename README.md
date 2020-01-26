# SlickText Contact Downloader

SlickText.com does not let you download your contact list so I created this Java program that will use the slicktext REST API to get all of the contacts including their first and last name, phone number, city, and opt-in method. It takes all this data and sorts them into a google sheets form using the google sheets API. 

## Installation

This program was created to custom work for one time use but it is fairly straight forward to download it and run it yourself. The things you will need to change are:

/src/main/java/Main.java:

```java
final String spreadsheetId = "Your spreadsheet ID";
Unirest.config().setDefaultBasicAuth("Public SlickText API Key", "Private SlickText API Key");
```

You will also need to delete the "tokens" folder and replace the src/main/resources/credenrials.json with your own from googles spreedsheet API.

## Usage

Once you change out the above fields you can run the program and it will do the rest. 

Note:

-A max of 10,000 contacts can be retrieved from the Slick Text API.

-Between each contact being inserted into the google sheet and 2.5 second pause is added to prevent going over googles API request limit.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change, or contact me on discord: P_O_G#2222.


## License
MIT License

Copyright (c) 2020 Cameron Smith

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
