//
//  WriteEvalViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditPictureView.h"

@interface WriteEvalViewController : UIViewController<EditPictureViewDelegate>

@property (weak, nonatomic) IBOutlet UIButton *btnFrontEval;
@property (weak, nonatomic) IBOutlet UIButton *btnBackEval;
@property (weak, nonatomic) IBOutlet UIButton *btnDetailEval;
@property (weak, nonatomic) UIButton *btnQuickEval;
@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (weak, nonatomic) IBOutlet UIButton *btnPersonal;
@property (weak, nonatomic) IBOutlet UIButton *btnEnterprise;
@property (weak, nonatomic) IBOutlet UITextView *txtViewReason;
@property (weak, nonatomic) IBOutlet UITextView *txtViewContent;
@property (weak, nonatomic) IBOutlet UILabel *lblEvalName;
@property (weak, nonatomic) IBOutlet UILabel *lblReasonCnt;
@property (weak, nonatomic) IBOutlet UILabel *lblContentCnt;

@property (weak, nonatomic) IBOutlet EditPictureView *editPictureView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightPictureView;
@property (nonatomic, weak) IBOutlet UIView *selectTypeView;
@property (nonatomic, weak) IBOutlet UIScrollView *writeEvalScrollView;
@property (nonatomic, weak) IBOutlet UIButton *accountSelectButton;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightReasonView;

- (IBAction)onClickFrontEval:(id)sender;
- (IBAction)onClickBackEval:(id)sender;
- (IBAction)onClickDetailEval:(id)sender;
- (IBAction)onClickQuickEval:(id)sender;
- (IBAction)onClickNavBackButton:(id)sender;
- (IBAction)onClickPublishButton:(id)sender;

@property (nonatomic, assign) BOOL isEvaluate;
@property (nonatomic, strong) NSString *evalId;
@property (nonatomic, strong) NSString *evalName;
@property (nonatomic, strong) NSDictionary *evalAccountDictionary;
@end
