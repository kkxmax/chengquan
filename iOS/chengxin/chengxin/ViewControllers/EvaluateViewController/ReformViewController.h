//
//  ReformViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ScrollPictureView.h"

@interface ReformViewController : UIViewController
@property ScrollPictureView *scrollPictureView;
@property (weak, nonatomic) IBOutlet UIView *viewContent;

@end
