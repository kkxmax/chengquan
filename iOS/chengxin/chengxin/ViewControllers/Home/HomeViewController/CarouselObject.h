//
//  CarouselObject.h
//  chengxin
//
//  Created by seniorcoder on 11/6/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CarouselObject : NSObject
{
}
@property (nonatomic, weak) NSString *objectID;
@property (nonatomic, weak) NSString *imageTitle;
@property (nonatomic, weak) NSString *videoTitle;
@property (nonatomic, weak) NSString *imageUrl;
@property (nonatomic, weak) NSString *videoUrl;

@end
