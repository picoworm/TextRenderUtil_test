TextRenderUtil_test
===================

Bug testing of font rendering with jogl curve library

Show 2 bugs of JOGL:
Library: jogl-2.2-b1103-20140709 windows amd64: 

First

The functions: 
1:  font.getGlyph(' ').getAdvance(fontSize, true); 
2:  font.getAdvanceWidth(Glyph.ID_SPACE, fontSize); 
return different result

Both of them must return the size of space character. 

this 2 functions is used inside jogl to determine width.
I looked inside source, and see that first function is used in font.getMetricBounds function to get size of string. 
And second one is used in drawString3D, to render the string 

So font.getMetricBounds return wrong value when space character inside a string

Second:

1. Q letter of arialbd font is overpainted
2. Some curves is abrupt (this can be seen with q and o chars)
3. Bold fonts (arialbd, timesbd) not displayed correctly.

