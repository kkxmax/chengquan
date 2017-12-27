//
//  ScrollPictureView.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScrollPictureView : UIScrollView
@property (nonatomic, strong) NSMutableArray  *aryPicture;
@property (weak, nonatomic) NSString        *strAddPic;

-(id) initWithFrame:(CGRect)frame :(NSMutableArray*)pictures;

@end
