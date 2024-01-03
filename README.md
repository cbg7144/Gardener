# Gardener 🪴
> ### Contributor: Seohee Yoon, Baekgyun Cho
> 2023.12.28 ~ 2024.01.03 <br />
*for 2023 Winter Madcamp Week01 Project* <br/>

<p>
<img alt="Java" src="https://img.shields.io/badge/Java-007396.svg?&style=for-the badge&logo=Java&logoColor=white"/> 
<img alt="Android" src="https://img.shields.io/badge/Android-3DDC84.svg?&style=for-the badge&logo=Android&logoColor=white"/> 
</p>
<img src="https://github.com/cbg7144/Gardener/assets/102652293/b339a5c8-4d0b-40c4-8284-38bc668831e2" width="300px" height="300px"/>

## 📂 About Projects
<img src="https://github.com/cbg7144/Gardener/assets/128574611/b3c73bc5-00f3-40f6-8afe-2776642a9fdd" width="768px" height="432px"/> <br />
**A plant management app with multiple functions - contacts, gallery, and where you can manage plants according to the weather!**

In a first project of KAIST MadCamp, we developed an Android app with three tabs. The first tab uses JSON format to build random contact data. The second tab builds my own gallery. The third tab is a free topic, and we organized a screen to create my own to-do list related to plants according to the concept.

### 1️⃣ Splash
<img src="https://github.com/cbg7144/Gardener/assets/128574611/face9079-f44d-488a-b0fd-2661c041f2c3" width="300px" height="600px"/> <br />
- Splash screen using avd file. You can see pop out animated logo when you launch the app.
   
### 2️⃣ Tab1 : Contact
<img src="https://github.com/cbg7144/Gardener/assets/128574611/e4a9bafa-dff8-4880-aa2b-2249b7d2c617" width="300px" height="600px"/> <br />
- You can add new contact by using bottom floating button and add new contact by typing string of name and phone number. Modified contacts are saved after reboot using shared preferences. You can search contact by name and phone number.
- You can call directly from contact and send a message, by touching call and message image located in right side of each ite getting call permission, ACTION_CALL
- You can remove contact by sliding each contact left

### 3️⃣ Tab2 : Gallery
<img src="https://github.com/cbg7144/Gardener/assets/128574611/b077421a-89f0-49de-9a6d-d3933623a077" width="300px" height="600px"/> <br />
 - Gallery tap initially contains 20 images of flowers and gardens. Grid view with 3 coulmns is implemented to display images.
   When you click an image, it opens up a horizontally oriented recycler view that displays the raw image files.
   You can easliy navigate to oher images by scrolling, and the scrolling appears to be continuous in both left and right directions. <br/>

 - There are two buttons at the bottom right. If you tap the buttons, access permission is requested. After the permission approved, you can use these buttons.
   1. Gallery Button
    Allow to add multiple images to gallery loacted in external storage.  
   2. Camera Button
    Allow to take picture from camera and the image is directly added in gallery.
    Also the image that we take is at external storage.

  - If you click on an image for a long time, you can choose whether to erase the picture.

### 4️⃣ Tab3 : Todo & Check weather
<img src="https://github.com/cbg7144/Gardener/assets/128574611/01f89317-e7c3-4269-b559-e522c08c0e93" width="300px" height="600px"/> <br />
  - The upper box recommends methods to care plants depending on the city location. If you input the city name, you can see the weather at the center and the method with blue color below.
    It is implemented by using OpenWeather API.

  - When you tap the bottom "SEE" button, you can see this week weather of the city

  - In the middle box, you can use "To do list" tab. You can add works to do, check works you did, delete works you want.

    
## 🧑‍🤝‍🧑 Contributor
<table border="" cellspacing="0" cellpadding="0" width="100%">
 <tr width="100%">
  <td align="center">Seohee Yoon</a></td>
  <td align="center">Baekgyun Cho</a></td>
 </tr>
 <tr>
  <td  align="center"><a href="mailto:appleid21@sookmyung.ac.kr"><img src="https://github.com/cbg7144/Gardener/assets/102652293/4607f870-f17f-4b55-993f-e7d4700131e0" border="0" width="90px"></a></td>
  <td  align="center"><a href="mailto:cbg7144@kaist.ac.kr"><img src="https://github.com/cbg7144/Gardener/assets/102652293/cbc19b9a-4fea-47c1-9640-fc007bf10bc0" border="0" width="90px"></a></td>
 </tr>
 <tr width="100%">
  <td  align="center"><a href="mailto:appleid21@sookmyung.ac.kr">appleid21@sookmyung.ac.kr</a></td>
  <td  align="center"><a href="mailto:cbg7144@kaist.ac.kr">cbg7144@kaist.ac.kr</a></td>
 </tr>
 <tr width="100%">
       <td  align="center"><p>Tab1 : Contact</p><p>Tab3 : To-do</p><p>Tab to tab connection</p></td>
       <td  align="center"><p>Tab2 : Gallery</p><p>Open Weather API</p><p>Splash</p></td>
     </tr>
  </table>

## ⬇️ Installation 
