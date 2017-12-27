//
//  ReformViewController.h
//  chengxin
//
//  Created by seniorcoder on 10/27/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "EditPictureView.h"

@interface ReformViewController : UIViewController<UITextViewDelegate, EditPictureViewDelegate>

@property (nonatomic, strong) NSDictionary *reformAccountDic;
@property (nonatomic) BOOL isOwnerFlag;
@property (weak, nonatomic) IBOutlet EditPictureView *editPictureView;
@property (weak, nonatomic) IBOutlet UIView *viewContent;
@property (weak, nonatomic) IBOutlet UILabel *evaluateReceiverLabel;
@property (weak, nonatomic) IBOutlet UILabel *evaluateSenderLabel;
@property (weak, nonatomic) IBOutlet UITextView *evaluateContentTextView;
@property (weak, nonatomic) IBOutlet UIButton *evaluateTypeButton1;
@property (weak, nonatomic) IBOutlet UIButton *evaluateTypeButton2;
@property (weak, nonatomic) IBOutlet UITextView *errorReasonTextView;
@property (weak, nonatomic) IBOutlet UITextView *errorBaseTextView;
@property (weak, nonatomic) IBOutlet UILabel *errorReasonLimitLabel;
@property (weak, nonatomic) IBOutlet UILabel *errorBaseLimitLabel;
@property (weak, nonatomic) IBOutlet UIView *addButtonView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *heightPicView;

@end
