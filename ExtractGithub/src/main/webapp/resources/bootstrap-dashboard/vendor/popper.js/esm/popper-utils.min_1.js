/*
 Copyright (C) Federico Zivolo 2017
 Distributed under the MIT License (license terms are at http://opensource.org/licenses/MIT).
 */function a(a,b){if(1!==a.nodeType)return[];var c=getComputedStyle(a,null);return b?c[b]:c}function b(a){return'HTML'===a.nodeName?a:a.parentNode||a.host}function c(d){if(!d)return document.body;switch(d.nodeName){case'HTML':case'BODY':return d.ownerDocument.body;case'#document':return d.body;}var e=a(d),f=e.overflow,g=e.overflowX,h=e.overflowY;return /(auto|scroll)/.test(f+h+g)?d:c(b(d))}function d(b){var c=b&&b.offsetParent,e=c&&c.nodeName;return e&&'BODY'!==e&&'HTML'!==e?-1!==['TD','TABLE'].indexOf(c.nodeName)&&'static'===a(c,'position')?d(c):c:b?b.ownerDocument.documentElement:document.documentElement}function e(a){var b=a.nodeName;return'BODY'!==b&&('HTML'===b||d(a.firstElementChild)===a)}function f(a){return null===a.parentNode?a:f(a.parentNode)}function g(a,b){if(!a||!a.nodeType||!b||!b.nodeType)return document.documentElement;var c=a.compareDocumentPosition(b)&Node.DOCUMENT_POSITION_FOLLOWING,h=c?a:b,i=c?b:a,j=document.createRange();j.setStart(h,0),j.setEnd(i,0);var k=j.commonAncestorContainer;if(a!==k&&b!==k||h.contains(i))return e(k)?k:d(k);var l=f(a);return l.host?g(l.host,b):g(a,f(b).host)}function h(a){var b=1<arguments.length&&arguments[1]!==void 0?arguments[1]:'top',c='top'===b?'scrollTop':'scrollLeft',d=a.nodeName;if('BODY'===d||'HTML'===d){var e=a.ownerDocument.documentElement,f=a.ownerDocument.scrollingElement||e;return f[c]}return a[c]}function j(a,b){var c=2<arguments.length&&void 0!==arguments[2]&&arguments[2],d=h(b,'top'),e=h(b,'left'),f=c?-1:1;return a.top+=d*f,a.bottom+=d*f,a.left+=e*f,a.right+=e*f,a}function k(a,b){var c='x'===b?'Left':'Top',d='Left'==c?'Right':'Bottom';return parseFloat(a['border'+c+'Width'],10)+parseFloat(a['border'+d+'Width'],10)}var l,m=function(){return void 0==l&&(l=-1!==navigator.appVersion.indexOf('MSIE 10')),l};function n(a,b,c,d){return Math.max(b['offset'+a],b['scroll'+a],c['client'+a],c['offset'+a],c['scroll'+a],m()?c['offset'+a]+d['margin'+('Height'===a?'Top':'Left')]+d['margin'+('Height'===a?'Bottom':'Right')]:0)}function o(){var a=document.body,b=document.documentElement,c=m()&&getComputedStyle(b);return{height:n('Height',a,b,c),width:n('Width',a,b,c)}}var p=Object.assign||function(a){for(var b,c=1;c<arguments.length;c++)for(var d in b=arguments[c],b)Object.prototype.hasOwnProperty.call(b,d)&&(a[d]=b[d]);return a};function q(a){return p({},a,{right:a.left+a.width,bottom:a.top+a.height})}function r(b){var c={};if(m())try{c=b.getBoundingClientRect();var d=h(b,'top'),e=h(b,'left');c.top+=d,c.left+=e,c.bottom+=d,c.right+=e}catch(a){}else c=b.getBoundingClientRect();var f={left:c.left,top:c.top,width:c.right-c.left,height:c.bottom-c.top},g='HTML'===b.nodeName?o():{},i=g.width||b.clientWidth||f.right-f.left,j=g.height||b.clientHeight||f.bottom-f.top,l=b.offsetWidth-i,n=b.offsetHeight-j;if(l||n){var p=a(b);l-=k(p,'x'),n-=k(p,'y'),f.width-=l,f.height-=n}return q(f)}function s(b,d){var e=m(),f='HTML'===d.nodeName,g=r(b),h=r(d),i=c(b),k=a(d),l=parseFloat(k.borderTopWidth,10),n=parseFloat(k.borderLeftWidth,10),o=q({top:g.top-h.top-l,left:g.left-h.left-n,width:g.width,height:g.height});if(o.marginTop=0,o.marginLeft=0,!e&&f){var p=parseFloat(k.marginTop,10),s=parseFloat(k.marginLeft,10);o.top-=l-p,o.bottom-=l-p,o.left-=n-s,o.right-=n-s,o.marginTop=p,o.marginLeft=s}return(e?d.contains(i):d===i&&'BODY'!==i.nodeName)&&(o=j(o,d)),o}function t(a){var b=Math.max,c=a.ownerDocument.documentElement,d=s(a,c),e=b(c.clientWidth,window.innerWidth||0),f=b(c.clientHeight,window.innerHeight||0),g=h(c),i=h(c,'left'),j={top:g-d.top+d.marginTop,left:i-d.left+d.marginLeft,width:e,height:f};return q(j)}function u(c){var d=c.nodeName;return'BODY'===d||'HTML'===d?!1:!('fixed'!==a(c,'position'))||u(b(c))}function v(a,d,e,f){var h={top:0,left:0},i=g(a,d);if('viewport'===f)h=t(i);else{var j;'scrollParent'===f?(j=c(b(d)),'BODY'===j.nodeName&&(j=a.ownerDocument.documentElement)):'window'===f?j=a.ownerDocument.documentElement:j=f;var k=s(j,i);if('HTML'===j.nodeName&&!u(i)){var l=o(),m=l.height,n=l.width;h.top+=k.top-k.marginTop,h.bottom=m+k.top,h.left+=k.left-k.marginLeft,h.right=n+k.left}else h=k}return h.left+=e,h.top+=e,h.right-=e,h.bottom-=e,h}function w(a){var b=a.width,c=a.height;return b*c}function x(a,b,c,d,e){var f=5<arguments.length&&arguments[5]!==void 0?arguments[5]:0;if(-1===a.indexOf('auto'))return a;var g=v(c,d,f,e),h={top:{width:g.width,height:b.top-g.top},right:{width:g.right-b.right,height:g.height},bottom:{width:g.width,height:g.bottom-b.bottom},left:{width:b.left-g.left,height:g.height}},i=Object.keys(h).map(function(a){return p({key:a},h[a],{area:w(h[a])})}).sort(function(c,a){return a.area-c.area}),j=i.filter(function(a){var b=a.width,d=a.height;return b>=c.clientWidth&&d>=c.clientHeight}),k=0<j.length?j[0].key:i[0].key,l=a.split('-')[1];return k+(l?'-'+l:'')}for(var y='undefined'!=typeof window&&'undefined'!=typeof document,z=['Edge','Trident','Firefox'],A=0,B=0;B<z.length;B+=1)if(y&&0<=navigator.userAgent.indexOf(z[B])){A=1;break}function i(a){var b=!1;return function(){b||(b=!0,window.Promise.resolve().then(function(){b=!1,a()}))}}function C(a){var b=!1;return function(){b||(b=!0,setTimeout(function(){b=!1,a()},A))}}var D=y&&window.Promise,E=D?i:C;function F(a,b){return Array.prototype.find?a.find(b):a.filter(b)[0]}function G(a,b,c){if(Array.prototype.findIndex)return a.findIndex(function(a){return a[b]===c});var d=F(a,function(a){return a[b]===c});return a.indexOf(d)}function H(a){var b;if('HTML'===a.nodeName){var c=o(),d=c.width,e=c.height;b={width:d,height:e,left:0,top:0}}else b={width:a.offsetWidth,height:a.offsetHeight,left:a.offsetLeft,top:a.offsetTop};return q(b)}function I(a){var b=getComputedStyle(a),c=parseFloat(b.marginTop)+parseFloat(b.marginBottom),d=parseFloat(b.marginLeft)+parseFloat(b.marginRight),e={width:a.offsetWidth+d,height:a.offsetHeight+c};return e}function J(a){var b={left:'right',right:'left',bottom:'top',top:'bottom'};return a.replace(/left|right|bottom|top/g,function(a){return b[a]})}function K(a,b,c){c=c.split('-')[0];var d=I(a),e={width:d.width,height:d.height},f=-1!==['right','left'].indexOf(c),g=f?'top':'left',h=f?'left':'top',i=f?'height':'width',j=f?'width':'height';return e[g]=b[g]+b[i]/2-d[i]/2,e[h]=c===h?b[h]-d[j]:b[J(h)],e}function L(a,b,c){var d=g(b,c);return s(c,d)}function M(a){for(var b=[!1,'ms','Webkit','Moz','O'],c=a.charAt(0).toUpperCase()+a.slice(1),d=0;d<b.length-1;d++){var e=b[d],f=e?''+e+c:a;if('undefined'!=typeof document.body.style[f])return f}return null}function N(a){return a&&'[object Function]'==={}.toString.call(a)}function O(a,b){return a.some(function(a){var c=a.name,d=a.enabled;return d&&c===b})}function P(a,b,c){var d=F(a,function(a){var c=a.name;return c===b}),e=!!d&&a.some(function(a){return a.name===c&&a.enabled&&a.order<d.order});if(!e){var f='`'+b+'`';console.warn('`'+c+'`'+' modifier is required by '+f+' modifier in order to work, be sure to include it before '+f+'!')}return e}function Q(a){return''!==a&&!isNaN(parseFloat(a))&&isFinite(a)}function R(a){var b=a.ownerDocument;return b?b.defaultView:window}function S(a,b){return R(a).removeEventListener('resize',b.updateBound),b.scrollParents.forEach(function(a){a.removeEventListener('scroll',b.updateBound)}),b.updateBound=null,b.scrollParents=[],b.scrollElement=null,b.eventsEnabled=!1,b}function T(a,b,c){var d=void 0===c?a:a.slice(0,G(a,'name',c));return d.forEach(function(a){a['function']&&console.warn('`modifier.function` is deprecated, use `modifier.fn`!');var c=a['function']||a.fn;a.enabled&&N(c)&&(b.offsets.popper=q(b.offsets.popper),b.offsets.reference=q(b.offsets.reference),b=c(b,a))}),b}function U(a,b){Object.keys(b).forEach(function(c){var d=b[c];!1===d?a.removeAttribute(c):a.setAttribute(c,b[c])})}function V(a,b){Object.keys(b).forEach(function(c){var d='';-1!==['width','height','top','right','bottom','left'].indexOf(c)&&Q(b[c])&&(d='px'),a.style[c]=b[c]+d})}function W(a,b,d,e){var f='BODY'===a.nodeName,g=f?a.ownerDocument.defaultView:a;g.addEventListener(b,d,{passive:!0}),f||W(c(g.parentNode),b,d,e),e.push(g)}function X(a,b,d,e){d.updateBound=e,R(a).addEventListener('resize',d.updateBound,{passive:!0});var f=c(a);return W(f,'scroll',d.updateBound,d.scrollParents),d.scrollElement=f,d.eventsEnabled=!0,d}var Y={computeAutoPlacement:x,debounce:E,findIndex:G,getBordersSize:k,getBoundaries:v,getBoundingClientRect:r,getClientRect:q,getOffsetParent:d,getOffsetRect:H,getOffsetRectRelativeToArbitraryNode:s,getOuterSizes:I,getParentNode:b,getPopperOffsets:K,getReferenceOffsets:L,getScroll:h,getScrollParent:c,getStyleComputedProperty:a,getSupportedPropertyName:M,getWindowSizes:o,isFixed:u,isFunction:N,isModifierEnabled:O,isModifierRequired:P,isNumeric:Q,removeEventListeners:S,runModifiers:T,setAttributes:U,setStyles:V,setupEventListeners:X};export{x as computeAutoPlacement,E as debounce,G as findIndex,k as getBordersSize,v as getBoundaries,r as getBoundingClientRect,q as getClientRect,d as getOffsetParent,H as getOffsetRect,s as getOffsetRectRelativeToArbitraryNode,I as getOuterSizes,b as getParentNode,K as getPopperOffsets,L as getReferenceOffsets,h as getScroll,c as getScrollParent,a as getStyleComputedProperty,M as getSupportedPropertyName,o as getWindowSizes,u as isFixed,N as isFunction,O as isModifierEnabled,P as isModifierRequired,Q as isNumeric,S as removeEventListeners,T as runModifiers,U as setAttributes,V as setStyles,X as setupEventListeners};export default Y;
//# sourceMappingURL=popper-utils.min.js.map
