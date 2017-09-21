# City SDK to Sights

- **Author**: Valter Nepomuceno
- **When**: September, 2017
- **Based in**: Lisbon, Portugal
- **Current Job**: Software Developer
- **Email Address**: valter.nep@gmail.com
- **Reference Links**: [LinkedIn](https://pt.linkedin.com/in/valternepomuceno) | [GitHub](https://github.com/Vnepomuceno) | [Facebook](https://www.facebook.com/valter.nepomuceno)

## Description

CitySDKToSights is a software component that queries [CitySDK](https://www.citysdk.eu/) API, transforming and storing its data into a MongoDB database to be further used by [Sights](https://play.google.com/store/apps/details?id=pt.sights), mobile application.

Below is an example of response from the CitySDK Tourism API when querying the ```/pois/search``` resource.

```json
{
      "location": {
        "point": [
          {
            "Point": {
              "posList": "38.7123638082457 -9.13036785646916",
              "srsName": "http:\/\/www.opengis.net\/def\/crs\/EPSG\/0\/4326"
            },
            "term": "entrance"
          }
        ],
        "address": {
          "value": "BEGIN:VCARD\r\nVERSION:2.1\r\nN:;Miradouro das Portas do Sol;;;;\r\nADR;WORK:;Largo das Portas do Sol\n1100-411 LISBOA;;Lisboa;;;Portugal\r\nTEL;WORK:+351 213 912 600\r\nEMAIL;INTERNET:daev@cm-lisboa.pt\r\nURL;WORK:\r\nEND:VCARD\r\n",
          "type": "text\/vcard"
        },
        "relationship": [
          {
            "targetPOI": "52d7bf4c723e8e0b0cc08b4f",
            "term": "within",
            "base": "http:\/\/tourism.citysdk.cm-lisboa.pt\/pois\/"
          }
        ]
      },
      "label": [
        {
          "term": "primary",
          "value": "Miradouro das Portas do Sol",
          "lang": "pt-PT"
        },
        {
          "term": "primary",
          "value": "Portas do Sol\ufffds Viewpoint",
          "lang": "en-GB"
        }
      ],
      "description": [
        {
          "value": "Localizado no Largo das Portas do Sol, da\u00ed o seu nome, fronteiro ao Pal\u00e1cio Azurara, onde est\u00e1 instalada \u00e0 Funda\u00e7\u00e3o Ricardo Esp\u00edrito Santo Silva, \u00e9 um balc\u00e3o privilegiado sobre a colina de S\u00e3o Vicente e um dos pontos mais cenogr\u00e1ficos da cidade. Dali avistamos as Igrejas de S\u00e3o Miguel e de Santo Est\u00eav\u00e3o, a Igreja e Mosteiro de S\u00e3o Vicente de Fora, o labir\u00edntico Bairro de Alfama, com as suas ruas e ruelas sinuosas, p\u00e1tios e travessas, escadarias e desn\u00edveis, que se estendem at\u00e9 ao Rio Tejo.\r\n",
          "lang": "pt-PT"
        }
      ],
      "category": [
        {
          "term": "category",
          "value": "Turismo - Miradouros",
          "lang": "pt-PT"
        }
      ],
      "time": [
        {
          "term": "open",
          "value": "24h"
        }
      ],
      "link": [
        {
          "term": "related",
          "href": "http:\/\/www.cm-lisboa.pt\/uploads\/pics\/tt_address\/miradouro-portas-do-sol_ASR2034.jpg",
          "type": "image\/jpeg"
        },
        {
          "term": "related",
          "href": "http:\/\/www.cm-lisboa.pt\/uploads\/pics\/tt_address\/miradouro-portas-do-sol_ASR2037.jpg",
          "type": "image\/jpeg"
        },
        {
          "term": "related",
          "href": "http:\/\/www.cm-lisboa.pt\/uploads\/pics\/tt_address\/miradouro-portas-do-sol_ASR2039.jpg",
          "type": "image\/jpeg"
        },
        {
          "term": "related",
          "href": "http:\/\/www.cm-lisboa.pt\/uploads\/pics\/tt_address\/miradouro-portas-do-sol_ASR2042.jpg",
          "type": "image\/jpeg"
        }
      ],
      "id": "52d7bf7e723e8e0b0cc0913b",
      "base": "http:\/\/tourism.citysdk.cm-lisboa.pt\/pois\/",
      "lang": "pt-PT",
      "updated": "2014-03-11T14:12:43.8540000Z",
      "created": "2013-12-10T12:05:13.0000000Z",
      "author": {
        "term": "primary",
        "value": "CitySDK"
      },
      "license": {
        "term": "primary",
        "value": "open-data"
      }
    }
```

Follows an example of a MongoDB document resultant from processing the above response with CitySDKToSights.

```json
{
    "_id": {
        "$oid": "59c40ba1dd5e3d03d9ac26d0"
    },
    "labels": [
        {
            "term": "primary",
            "value": "Miradouro das Portas do Sol",
            "lang": "pt-PT",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        },
        {
            "term": "primary",
            "value": "Portas do Sol's Viewpoint",
            "lang": "en-GB",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        }
    ],
    "descriptions": [
        {
            "value": "Localizado no Largo das Portas do Sol, daí o seu nome, fronteiro ao Palácio Azurara, onde está instalada à Fundação Ricardo Espírito Santo Silva, é um balcão privilegiado sobre a colina de São Vicente e um dos pontos mais cenográficos da cidade. Dali avistamos as Igrejas de São Miguel e de Santo Estêvão, a Igreja e Mosteiro de São Vicente de Fora, o labiríntico Bairro de Alfama, com as suas ruas e ruelas sinuosas, pátios e travessas, escadarias e desníveis, que se estendem até ao Rio Tejo.\r\n",
            "lang": "pt-PT",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        }
    ],
    "images": [
        {
            "href": "http://www.cm-lisboa.pt/uploads/pics/tt_address/miradouro-portas-do-sol_ASR2034.jpg",
            "type": "image/jpeg",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        },
        {
            "href": "http://www.cm-lisboa.pt/uploads/pics/tt_address/miradouro-portas-do-sol_ASR2037.jpg",
            "type": "image/jpeg",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        },
        {
            "href": "http://www.cm-lisboa.pt/uploads/pics/tt_address/miradouro-portas-do-sol_ASR2039.jpg",
            "type": "image/jpeg",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        },
        {
            "href": "http://www.cm-lisboa.pt/uploads/pics/tt_address/miradouro-portas-do-sol_ASR2042.jpg",
            "type": "image/jpeg",
            "createdAt": "Thu Sep 21 19:57:31 WEST 2017",
            "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
        }
    ],
    "contact": {
        "phoneNumber": "+351 213 912 600",
        "emailAddress": "daev@cm-lisboa.pt"
    },
    "schedules": [
        {
            "open": "24h",
            "createdAt": "Thu Sep 21 23:28:43 WEST 2017",
            "updatedAt": "Thu Sep 21 23:28:43 WEST 2017"
        }
    ],
    "location": {
        "address": "Largo das Portas do Sol",
        "city": "Lisbon",
        "country": "Portugal",
        "coordinates": "38.7123638082457 -9.13036785646916",
        "createdAt": "Thu Sep 21 23:25:06 WEST 2017",
        "updatedAt": "Thu Sep 21 23:25:06 WEST 2017"
    },
    "author": {
        "source": "open-data",
        "createdAt": "Thu Sep 21 19:57:31 WEST 2017"
    },
    "citySdkId": "52d7bf7e723e8e0b0cc0913b",
    "base": "http://tourism.citysdk.cm-lisboa.pt/pois/",
    "createdAt": "Tue Mar 11 16:35:03 WET 2014",
    "updatedAt": "Thu Sep 21 19:57:31 WEST 2017"
}
```