//
//  ImageChooseViewController.h
//  chengxin
//
//  Created by common on 3/9/16.
//  Copyright © 2016 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ImageChooseViewController;

@protocol ImageChooseViewControllerDelegate
-(void)chooseViewController:(ImageChooseViewController*)vc shownImage:(UIImage*)image;
@end

@interface ImageChooseViewController : UIViewController <UINavigationControllerDelegate, UIImagePickerControllerDelegate>

@property (nonatomic, retain) IBOutlet UIView* chooseView;
@property (nonatomic, weak) id<ImageChooseViewControllerDelegate> delegate;
@property (nonatomic, weak) UINavigationController* navController;
@property (nonatomic) BOOL isSquareCrop;
@property (nonatomic, assign) BOOL      notEdit;//是否不裁剪 默认裁剪

-(IBAction)onCamera:(id)sender;
-(IBAction)onGallery:(id)sender;
-(IBAction)onCancel:(UITapGestureRecognizer*)recognizer;
@end
