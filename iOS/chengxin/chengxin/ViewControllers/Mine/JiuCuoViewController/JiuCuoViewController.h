//
//  JiuCuoViewController.h
//  chengxin
//
//  Created by seniorcoder on 11/3/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JiuCuoViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIScrollView *scrollResonView;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollPersonView;

@property (weak, nonatomic) IBOutlet UIImageView *imgAuth;
@property (weak, nonatomic) IBOutlet UIButton *btnKuiDa;
@property (weak, nonatomic) IBOutlet UIButton *btnHuJia;
@property (weak, nonatomic) IBOutlet UITextView *lblReason;
@property (weak, nonatomic) IBOutlet UITextView *lblWhy;
@property (weak, nonatomic) IBOutlet UILabel *lbl;
@property (weak, nonatomic) IBOutlet UIImageView *imgEstimaterPhoto;
@property (weak, nonatomic) IBOutlet UIImageView *imgBusinessType;
@property (weak, nonatomic) IBOutlet UILabel *lblEstimaterName;
@property (weak, nonatomic) IBOutlet UILabel *lblEstimaterComment;
@property (weak, nonatomic) IBOutlet UILabel* lblTime;

@property (nonatomic, retain) NSDictionary* data;
@end
