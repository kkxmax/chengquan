//
//  WriteEvalViewController.m
//  chengxin
//
//  Created by seniorcoder on 10/26/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import "WriteEvalViewController.h"
#import "SelectEvaluatorVC.h"
#import "Global.h"

@interface WriteEvalViewController () <UITextViewDelegate>
{
    NSMutableArray *aryEvalPicture;
    NSInteger mKind;
    NSInteger mMethod;
}
@end

@implementation WriteEvalViewController
@synthesize btnFrontEval, btnBackEval, btnDetailEval, btnQuickEval;
@synthesize editPictureView, heightPictureView;
@synthesize btnPersonal, btnEnterprise, txtViewReason, txtViewContent, lblEvalName;
@synthesize evalId, evalName;
@synthesize lblContentCnt, lblReasonCnt;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    aryEvalPicture = [[NSMutableArray alloc] init];
    mKind = 1;
    mMethod = 1;
    evalId = @"";
    evalName = @"";
    
    CGRect rect = editPictureView.frame;
    [editPictureView setFrame:rect :aryEvalPicture];
    editPictureView.delegate = self;

    heightPictureView.constant = editPictureView.frame.size.height;
    
    lblReasonCnt.text = @"0/100";
    lblContentCnt.text = @"0/100";
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    lblEvalName.text = evalName;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)onClickFrontEval:(id)sender {
    [btnFrontEval setSelected:YES];
    [btnBackEval setSelected:NO];
    
    mKind = 1;
}

- (IBAction)onClickBackEval:(id)sender {
    [btnFrontEval setSelected:NO];
    [btnBackEval setSelected:YES];
    
    mKind = 2;
}

- (IBAction)onClickDetailEval:(id)sender {
    [btnDetailEval setSelected:YES];
    [btnQuickEval setSelected:NO];
    
    mMethod = 1;
}

- (IBAction)onClickQuickEval:(id)sender {
    [btnDetailEval setSelected:NO];
    [btnQuickEval setSelected:YES];
    
    mMethod = 2;
}

- (IBAction)onClickNavBackButton:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)onClickEvalNameButton:(id)sender {
    SelectEvaluatorVC *vc = [[SelectEvaluatorVC alloc] initWithNibName:@"SelectEvaluatorVC" bundle:nil];
    vc.selectType = mKind;
    vc.rootVC = self;
    [self.navigationController pushViewController:vc animated:YES];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    return textView.text.length + (text.length - range.length) <= 100;
}

- (void)textViewDidChange:(UITextView *)textView {
    if (textView == self.txtViewReason)
        self.lblReasonCnt.text = [NSString stringWithFormat:@"%d/100", (int)textView.text.length];
    else if (textView == self.txtViewContent)
        self.lblContentCnt.text = [NSString stringWithFormat:@"%d/100", (int)textView.text.length];
}


- (IBAction)onClickPersonalButton:(id)sender {
    [btnPersonal setBackgroundImage:[UIImage imageNamed:@"yellow-button"] forState:UIControlStateNormal];
    [btnPersonal setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
    
    [btnEnterprise setBackgroundColor:BLACK_COLOR_245];
    [btnEnterprise setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnEnterprise setBackgroundImage:nil forState:UIControlStateNormal];
}

- (IBAction)onClickEnterpriseButton:(id)sender {
    [btnEnterprise setBackgroundImage:[UIImage imageNamed:@"yellow-button"] forState:UIControlStateNormal];
    [btnEnterprise setTitleColor:WHITE_COLOR forState:UIControlStateNormal];
    
    [btnPersonal setBackgroundColor:BLACK_COLOR_245];
    [btnPersonal setTitleColor:BLACK_COLOR_102 forState:UIControlStateNormal];
    [btnPersonal setBackgroundImage:nil forState:UIControlStateNormal];
}

- (IBAction)onClickPublishButton:(id)sender {
    [self addEvaluateToServer];
}

-(void) changedPictureView:(NSMutableArray *)aryPicture :(int)height {
    heightPictureView.constant = height;
    aryEvalPicture = aryPicture;
}

- (void) addEvaluateToServer {
    
    NSMutableDictionary *dicParams = [[NSMutableDictionary alloc] init];
    
    [dicParams setObject:@"leaveEstimate" forKey:@"pAct"];
    [dicParams setObject:[CommonData sharedInstance].tokenName forKey:@"token"];
    
    if (self.isEvaluate) {
        [dicParams setObject:@"1" forKey:@"type"];
        [dicParams setObject:evalId forKey:@"accountId"];
    }
    else {
        [dicParams setObject:@"2" forKey:@"type"];
        [dicParams setObject:evalId forKey:@"hotId"];
    }

    [dicParams setObject:[NSString stringWithFormat:@"%d", mKind] forKey:@"kind"];
    [dicParams setObject:[NSString stringWithFormat:@"%d", mMethod] forKey:@"method"];
    [dicParams setObject:txtViewReason.text forKey:@"reason"];
    [dicParams setObject:txtViewContent.text forKey:@"content"];
    
    for (int i = 0; i < aryEvalPicture.count; i++) {
        NSData* image = UIImagePNGRepresentation(aryEvalPicture[i]);
        [dicParams setObject:image forKey:[NSString stringWithFormat:@"contentImage%d", i]];
    }
    
    [[WebAPI sharedInstance] sendPostRequestWithUpload:ACTION_LEAVEESTIMATE Parameters:dicParams :^(NSObject *resObj) {
        NSDictionary *dicRes = (NSDictionary *)resObj;
        
        if (dicRes != nil ) {
            if ([dicRes[@"retCode"] intValue] == RESPONSE_SUCCESS) {
                [self.navigationController popViewControllerAnimated:YES];
            }
            else {
                [GeneralUtil alertInfo:dicRes[@"msg"]];
            }
        }
    }];
}

@end
