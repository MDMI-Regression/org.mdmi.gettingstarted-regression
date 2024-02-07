// import fetch from "node-fetch";


const message = 'Hello world'; // Try edit me

// const fetch = import("node-fetch");

function myFunction(systemUrl,p1) {

  var identifierObject = {
    system: "system",
    value: "46252"
  }

  var requestObject = {     
    method: "POST",
    url: "userData2",
    ifNoneExist: "Patient?identifier=http://acme.org/mrns|013872"
 
   }
  p1.type = 'transaction'
    

  Object.entries(p1).forEach(entry => {
    const [key, value] = entry;
    console.log(`${key}: ${value}`);

    if (key === 'entry') {
      Object.entries(value).forEach(entry => {
        const [key, value] = entry;
        console.log(`     ${key}: ${value}`);

        console.log(' url  ' +value.fullUrl)
        console.log(' resourceType  ' +value.resource.resourceType)

       
        value.fullUrl = value.resource.resourceType + '/' + value.resource.id


        value.request = JSON.parse(JSON.stringify(requestObject)) 

        value.resource.identifier = JSON.parse(JSON.stringify(identifierObject)) 

        value.resource.identifier.value = value.resource.id
        value.resource.identifier.system = systemUrl
       
       
        console.log(value.fullUrl);
        console.log(value.resource.resourceType);
        value.request.url =  value.resource.resourceType 
        // + '/' + value.resource.id
        value.request.ifNoneExist = value.resource.resourceType + '?identifier=' +systemUrl + '|' + value.resource.id
        console.log( value.request.url);

        // delete value.fullUrl;
        delete value.search;
        delete value.resource.id

        Object.entries(value).forEach(entry => {
          const [key, value] = entry;
        //   console.log(`       ${key}: ${value}`);

          if (key === 'resource') {
            Object.entries(value).forEach(entry => {
              const [key, value] = entry;
            //   console.log(`         ${key}: ${value}`);
            });
          }
        });
      });
    }
  });

 console.log('===================================');
   console.log(p1);
   console.log('+++++++++++++++++++++++++++');
//    console.log( JSON.stringify(p1 ,null, "\t"));

   var fs = require('fs');
fs.writeFile("test.json", JSON.stringify(p1 ,null, "\t"), function(err) {
    if (err) {
        console.log(err);
    }

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    var raw = JSON.stringify(p1)

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
      };
      
      import fetch from 'node-fetch';

      fetch("http://localhost:8080/fhir", requestOptions)
        .then(response => response.text())
        .then(result => console.log(result))
        .catch(error => console.log('error', error));


});

  
}


// fetch('https://api.example.com/data')
//   .then(response => response.json())
//   .then(data => console.log(data))
//   .catch(error => console.error(error));

// Log to console
console.log(message);

// var j

// fetch('https://hapi.fhir.org/baseR5/Patient/7477/$everything')
//   .then(response => response.json())
//   .then(json => {
//     myFunction('https://hapi.fhir.org/baseR5',json);
//   });


//   fetch('https://hapi.fhir.org/baseR5/Patient/95956/$everything')
//   .then(response => response.json())
//   .then(json => {
//     myFunction('https://hapi.fhir.org/baseR5',json);
//   });

//   fetch('https://gw.interop.community/SyntheaTest8/open/Patient/mcc-pat-pnoelle/$everything')
//   .then(response => response.json())
//   .then(json => {
//     myFunction('https://gw.interop.community/SyntheaTest8/open/',json);
//   });
 
// fetch('https://gw.interop.community/SyntheaTest8/open/Patient/mcc-pat-pnoelle/$everything')
//   .then(response => response.json())
//   .then(data => console.log(data))
//   .catch(error => console.error(error));

//   fetch('https://api.example.com/data')
//   .then(response => response.json())
//   .then(data => console.log(data))
//   .catch(error => console.error(error));


  const requestOptions = {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' } 
};

import fetch from 'node-fetch';

fetch('https://gw.interop.community/SyntheaTest8/open/Patient/mcc-pat-pnoelle/$everything', requestOptions)
    .then(response => response.json())
    .then(data => console.error(data) );