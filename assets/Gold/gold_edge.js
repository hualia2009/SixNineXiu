/**
 * Adobe Edge: symbol definitions
 */
(function($, Edge, compId){
//images folder
var im='images/';

var fonts = {};


var resources = [
];
var symbols = {
"stage": {
   version: "2.0.1",
   minimumCompatibleVersion: "2.0.0",
   build: "2.0.1.268",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: false,
   resizeInstances: false,
   content: {
         dom: [
         {
            id:'gold',
            type:'image',
            rect:['130px','257px','30px','20px','auto','auto'],
            fill:["rgba(0,0,0,0)",im+"gold.png",'0px','0px']
         }],
         symbolInstances: [

         ]
      },
   states: {
      "Base State": {
         "${_Stage}": [
            ["color", "background-color", 'rgba(255,255,255,0.00)'],
            ["style", "width", '320px'],
            ["style", "height", '240px'],
            ["style", "overflow", 'hidden']
         ],
         "${_gold}": [
            ["style", "top", '257px'],
            ["transform", "scaleX", '1'],
            ["transform", "scaleY", '1'],
            ["transform", "rotateZ", '0deg'],
            ["style", "height", '17px'],
            ["style", "opacity", '1'],
            ["style", "left", '130px'],
            ["style", "width", '24px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 2000,
         autoPlay: true,
         timeline: [
            { id: "eid23", tween: [ "style", "${_gold}", "opacity", '0', { fromValue: '1'}], position: 1500, duration: 500 },
            { id: "eid19", tween: [ "style", "${_gold}", "width", '24px', { fromValue: '24px'}], position: 0, duration: 0 },
            { id: "eid18", tween: [ "style", "${_gold}", "height", '17px', { fromValue: '17px'}], position: 0, duration: 0 },
            { id: "eid12", tween: [ "transform", "${_gold}", "scaleY", '1.24392', { fromValue: '1'}], position: 0, duration: 676 },
            { id: "eid20", tween: [ "transform", "${_gold}", "scaleY", '1.26473', { fromValue: '1.24392'}], position: 676, duration: 574 },
            { id: "eid17", tween: [ "transform", "${_gold}", "scaleY", '1.03057', { fromValue: '1.26473'}], position: 1250, duration: 250 },
            { id: "eid4", tween: [ "transform", "${_gold}", "scaleX", '1.67726', { fromValue: '1'}], position: 0, duration: 676 },
            { id: "eid11", tween: [ "transform", "${_gold}", "scaleX", '1.59031', { fromValue: '1.67726'}], position: 676, duration: 574 },
            { id: "eid16", tween: [ "transform", "${_gold}", "scaleX", '1.26529', { fromValue: '1.59031'}], position: 1250, duration: 250 },
            { id: "eid3", tween: [ "transform", "${_gold}", "rotateZ", '471deg', { fromValue: '0deg'}], position: 0, duration: 676 },
            { id: "eid7", tween: [ "transform", "${_gold}", "rotateZ", '479deg', { fromValue: '471deg'}], position: 676, duration: 74 },
            { id: "eid10", tween: [ "transform", "${_gold}", "rotateZ", '658deg', { fromValue: '479deg'}], position: 750, duration: 500 },
            { id: "eid15", tween: [ "transform", "${_gold}", "rotateZ", '1185deg', { fromValue: '658deg'}], position: 1250, duration: 250 },
            { id: "eid1", tween: [ "style", "${_gold}", "top", '100px', { fromValue: '257px'}], position: 0, duration: 676 },
            { id: "eid6", tween: [ "style", "${_gold}", "top", '98px', { fromValue: '100px'}], position: 676, duration: 74 },
            { id: "eid9", tween: [ "style", "${_gold}", "top", '83px', { fromValue: '98px'}], position: 750, duration: 500 },
            { id: "eid13", tween: [ "style", "${_gold}", "top", '-31px', { fromValue: '83px'}], position: 1250, duration: 250 },
            { id: "eid2", tween: [ "style", "${_gold}", "left", '58px', { fromValue: '130px'}], position: 0, duration: 676 },
            { id: "eid5", tween: [ "style", "${_gold}", "left", '70px', { fromValue: '58px'}], position: 676, duration: 74 },
            { id: "eid8", tween: [ "style", "${_gold}", "left", '87px', { fromValue: '70px'}], position: 750, duration: 500 },
            { id: "eid14", tween: [ "style", "${_gold}", "left", '38px', { fromValue: '87px'}], position: 1250, duration: 250 }         ]
      }
   }
}
};


Edge.registerCompositionDefn(compId, symbols, fonts, resources);

/**
 * Adobe Edge DOM Ready Event Handler
 */
$(window).ready(function() {
     Edge.launchComposition(compId);
});
})(jQuery, AdobeEdge, "EDGE-49812053");
