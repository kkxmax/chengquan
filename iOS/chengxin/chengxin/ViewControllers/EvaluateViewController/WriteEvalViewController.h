//
//  WriteEvalViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ScrollPictureView.h"

@interface WriteEvalViewController : UIViewController
@property (weak, nonatomic) IBOutlet UIButton *btnFrontEval;
@property (weak, nonatomic) IBOutlet UIButton *btnBackEval;
@property (weak, nonatomic) IBOutlet UIButton *btnDetailEval;
@property (weak, nonatomic) IBOutlet UIButton *btnQuickEval;
@property (weak, nonatomic) IBOutlet UIView *viewContent;

@property ScrollPictureView *scrollPictureView;

- (IBAction)onClickFrontEval:(id)sender;
- (IBAction)onClickBackEval:(id)sender;
- (IBAction)onClickDetailEval:(id)sender;
- (IBAction)onClickQuickEval:(id)sender;
- (IBAction)onClickNavBackButton:(id)sender;

@end
