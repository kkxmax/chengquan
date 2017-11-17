//
//  EvaluateDetailTableViewCell.h
//  chengxin
//
//  Created by seniorcoder on 11/1/17.
//  Copyright © 2017 chengxin. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface EvaluateDetailTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIScrollView *scrollThumb;
@property (weak, nonatomic) IBOutlet UIImageView *imgPhoto;
@property (weak, nonatomic) IBOutlet UILabel *lblTitle;
@property (weak, nonatomic) IBOutlet UILabel *lblContent;
@property (weak, nonatomic) IBOutlet UILabel *lblMore;
@property (weak, nonatomic) IBOutlet UILabel *lblDate;
@property (weak, nonatomic) IBOutlet UILabel *lblEval;


@end
