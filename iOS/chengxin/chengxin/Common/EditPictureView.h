//
//  EditPictureView.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ImageChooseViewController.h"

@protocol EditPictureViewDelegate;

@interface EditPictureView : UIView <ImageChooseViewControllerDelegate>

@property (nonatomic, strong) NSMutableArray  *aryPicture;
@property (weak, nonatomic) NSString        *strAddPic;
@property (nonatomic) NSInteger maximumPictureCount;
-(id) initWithFrame:(CGRect)frame :(NSMutableArray*)pictures;
-(void) setFrame:(CGRect)frame :(NSMutableArray*)pictures;
-(void)addPicture:(UIImage*)image;
-(void)reloadPictureData;

@property (assign, nonatomic) id <EditPictureViewDelegate>delegate;

@end

@protocol EditPictureViewDelegate<NSObject>

@optional
- (void)changedPictureView:(NSMutableArray*) aryPicture :(int) height;

@end
