//
//  ChengXinDegreeView.m
//  chengxin
//
//  Created by common on 7/28/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "ChengXinDegreeView.h"

#define PIE 3.141592f

@implementation ChengXinDegreeView

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
    CGContextRef contextRef = UIGraphicsGetCurrentContext();
    CGContextSetRGBFillColor(contextRef, 255, 255, 255, 0.1);
    //CGContextFillEllipseInRect(contextRef, CGRectMake(30, 30, 30, 30));
    
    CGRect viewBound = self.bounds;
    float radius = viewBound.size.width / 2.0f * 0.8;
    for(int i = -220 ; i < 40; i++)
    {
        CGContextFillEllipseInRect(contextRef, CGRectMake(viewBound.size.width/2 +  radius * cos(i / 360.0f * PIE * 2 ), viewBound.size.width/2 + radius * sin(i / 360.0f * PIE * 2), 5, 5));
    }
    
    CGContextSetRGBFillColor(contextRef, 255, 255, 255, 1);
    //CGContextFillEllipseInRect(contextRef, CGRectMake(30, 30, 30, 30));
    float percent = self.percent;
    int ep = -220 + 260 / 100.0f * percent;
    for(int i = -220 ; i < ep; i++)
    {
        CGContextFillEllipseInRect(contextRef, CGRectMake(viewBound.size.width/2 + radius * cos(i / 360.0f * PIE * 2 ), viewBound.size.width/2 + radius * sin(i / 360.0f * PIE * 2), 5, 5));
    }
    CGContextFillEllipseInRect(contextRef, CGRectMake(viewBound.size.width/2 + radius * cos(ep / 360.0f * PIE * 2 ) - 2.5, viewBound.size.width/2 + radius * sin(ep / 360.0f * PIE * 2) - 2.5, 10, 10));
    CGContextSetRGBFillColor(contextRef, 255, 255, 255, 0.5);
    CGContextFillEllipseInRect(contextRef, CGRectMake(viewBound.size.width/2 + radius * cos(ep / 360.0f * PIE * 2 ) - 5, viewBound.size.width/2 + radius * sin(ep / 360.0f * PIE * 2) - 5, 15, 15));
    CGContextSetRGBFillColor(contextRef, 255, 255, 255, 0.3);
    CGContextFillEllipseInRect(contextRef, CGRectMake(viewBound.size.width/2 + radius * cos(ep / 360.0f * PIE * 2 ) - 7.5, viewBound.size.width/2 + radius * sin(ep / 360.0f * PIE * 2) - 7.5, 20, 20));
}


@end
