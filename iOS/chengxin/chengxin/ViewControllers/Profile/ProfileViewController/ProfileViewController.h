//
//  ProfileViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVKit/AVKit.h>
#import <AVFoundation/AVFoundation.h>

@interface ProfileViewController : UIViewController
{
}
@property (nonatomic, strong) UIImage *selectedImage;
@property (nonatomic, strong) NSString *selectedName;

@property (nonatomic, strong) IBOutlet UIImageView *profileImageView;
@property (nonatomic, strong) IBOutlet UIView *videoPlayerView;

@end
